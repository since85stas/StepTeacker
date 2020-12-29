package stas.batura.stepteacker.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stas.batura.stepteacker.MainActivity
import stas.batura.stepteacker.data.IRep
import stas.batura.stepteacker.data.room.Pressure
import stas.batura.stepteacker.rx.chess.ChessClockRx
import stas.batura.stepteacker.rx.chess.ChessStateChageListner
import stas.batura.stepteacker.rx.rxZipper.Container
import stas.batura.stepteacker.utils.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class PressureService @Inject constructor(): LifecycleService(), SensorEventListener, LocationListener,
    ChessStateChageListner {

    /**
     * Job allows us to cancel all coroutines started by this ViewModel.
     */
    private var serviceJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this Service.
     */
    private val ioScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val TAG = PressureService::class.simpleName

    private val NOTIFICATION_ID = 21

    private val CHANNEL_ID = "PressCh"

    // interval between saves in seconds
    private val INTERVAL = 60L * 5

    private var timeCount: Long = -10L

    @Inject lateinit var sensorManager: SensorManager

    @Inject lateinit var repository: IRep

    // pressure sensor installed
    private var sensor: Sensor? = null

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false

    // Declaring a Location Manager
    @Inject lateinit var locationManager: LocationManager

//    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient

    // chess instance to calculate distance before saves
    private lateinit var chessClockRx: ChessClockRx

    // last rain power
    private var lastPower: Int = 0

    // begin of last day date
    private lateinit var lastDayBegin: Calendar

    private var lastAlt: Float = 170f

    private var locationCallback: LocationCallback? = null

    private var locationRequest: LocationRequest? = null

    private val HOME_ALT = 170.0f
    private val WORK_ALT = 163.0f
//    var fusedLocationClient: FusedLocationProviderClient? = null

    private val consumer = object : Consumer<Container> {
        override fun accept(t: Container?) {
            Log.d(TAG, "accept: consum")
            println(t)
            savePressureValue(t!!.pressure, t.altitude)
        }
    }

//    val zipper = Zipper(consumer)

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.d(TAG, "onCreate: " + deviceSensors.toString())

        lastDayBegin = getCurrentDayBegin()

        initPressSensor()

        createClock()

        startForeground(NOTIFICATION_ID, getNotification())

        lastPower = repository.getRainPower().lastPowr

//        val beginTime = lastDayBegin.timeInMillis
//        val endTime = getCurrentDayEnd(lastDayBegin).timeInMillis
//        isLocatRecieved = true
        Log.d(TAG, "onCreate: service")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * binding service to activirty
     */
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
                Log.d(TAG, "servise is bind " + intent.toString())
        this.PressureServiceBinder().isBind = true
        return PressureServiceBinder()
    }

    /**
     * unbinding service to activirty
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "servise is unbind " + intent.toString())
        this.PressureServiceBinder().isBind = false
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unregisterListn()
        stopClock()
    }

    /**
     * initianing a sensor manager
     */
    private fun initPressSensor() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            val gravSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_PRESSURE)
            // Use the version 3 gravity sensor.
            sensor = gravSensors.firstOrNull()
        } else {
            Toast.makeText(applicationContext, "Sensor not detected", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * saving value in DB
     */
    private fun savePressureValue(pressure: Float, altitude: Float) {
        val mmPres = pressure
        val roomPre = Pressure(mmPres, System.currentTimeMillis(), lastPower, altitude)
        Log.d(TAG, "savePressureValue: " + pressure +" " + altitude)
        repository.insertPressure(roomPre)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /**
     * getting a value from sensor
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val pressure = event.values
            unregisterListn()
            if (pressure.size > 0) {
//                zipper.generatePress(pressure[0])
                savePressureValue(pressure[0], saveFakeLastLocationValue())
            }
        }
    }

    /**
     * registring from sensor
     */
    private fun registerListn() {

        if (sensor != null) {
            sensor?.also { light ->
                sensorManager.registerListener(this, light, 100000)
            }
        } else {
            savePressureValue(1000.1f, 10.0f)
//            zipper.generatePress(1000.0f)
        }
    }

    /**
     * unregistring from sensor
     */
    private fun unregisterListn() {
        sensorManager.unregisterListener(this)
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: ")
    }

    //    override fun onLocationChanged(location: Location?) {
//        Log.d(TAG, "onLocationChanged: ")
//    }
//
//    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//    }
//
//    override fun onProviderEnabled(provider: String?) {
//    }
//
//    override fun onProviderDisabled(provider: String?) {
//    }

    /**
     * creates a new clock object
     */
    private fun createClock() {
//        if (chessClockRx != null ) {
//            chessClockRx.stopTimer()
//        }
        chessClockRx = ChessClockRx(INTERVAL, this)
        Log.d(TAG, "createClock: " + chessClockRx.toString())
    }

    /**
     * stoping clock fun
     */
    private fun stopClock() {
        chessClockRx.stopTimer()
    }

    /**
     * recieving a time from clock
     */
    override fun timeChange(time: Long) {
        timeCount += 10L
        val formatter = SimpleDateFormat("HH:mm:ss");
        val dateString = formatter.format( Calendar.getInstance().timeInMillis );
        Log.d(TAG, "timeChange: " + time + " " + dateString)
        if (timeCount == 0L) {
            if (true) {
                combineData()
                if (checkNextDay()) {
                    getPressrsForLastday()
                }
            } else {
                Log.d(TAG, "timeChange: saving off")
            }
        } else if (timeCount == INTERVAL - 10L) {
            timeCount = -10L
        }
    }

    fun combineData() {
        Log.d(TAG, "combineData: ")
        registerListn()
//        getLocationNew()
//        saveFakeLastLocationValue()
    }

    /**
     * finshing a time period
     */
    override fun timeFinish() {
        Log.d(TAG, "timeFinish: ")
//        createClock()
    }

    /**
     * create a Notification object
     */
    private fun getNotification(): Notification {

        createNotificationChannel()

        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(getIconId())
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Collecting pressure..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)
                .setVibrate(longArrayOf(0))

        return builder.build()
    }

    /**
     * get notif icon ID
     */
    private fun getIconId(): Int {
//        when (lastPower) {
//            0 -> return stas.batura.stepteacker.R.drawable.icon_0
//        }
        return stas.batura.stepteacker.R.drawable.icon_0
    }

    /**
     * updating notific icons
     */
    private fun updateNotification() {
        val notification = getNotification()
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * create a notification chanel
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "name"
            val description = "descr"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.enableVibration(false)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * create a service binder
     */
    inner class PressureServiceBinder : Binder() {

        var isBind: Boolean = false

        fun setRainPower(rainpower: Int) {
            this@PressureService.lastPower = rainpower
            updateNotification()
        }

        fun closeService() {
            this@PressureService.stopService()
        }

        fun getRainPower() : Int {
            return this@PressureService.lastPower
        }

        fun savePressure() {
            this@PressureService.combineData()
            return
        }

        fun testWrite() {
            this@PressureService.testWriteFile()
        }

//        fun testLocation() {
//            this@PressureService.getLocationNew()
//        }

        fun updateNotif() {
            this@PressureService.updateNotification()
        }

        fun testRx() {
//            this@PressureService.testRx()
        }

        fun getLastDayBeg(): Calendar {
            return this@PressureService.lastDayBegin
        }
    }

    /**
     * stopping service
     */
    fun stopService() {
        stopSelf()
    }

    /**
     * adding observers for presures
     */
    fun getPressrsForLastday() {
        ioScope.launch {
            val end = getCurrentDayEnd(lastDayBegin)
            val beginTime = lastDayBegin.timeInMillis
            val endTime = getCurrentDayEnd(lastDayBegin).timeInMillis
            val res =  repository.getPressuresInInterval(lastDayBegin.timeInMillis,
                getCurrentDayEnd(lastDayBegin).timeInMillis)
//            val res = repository.getPressures()
            Log.d(TAG, "getPressrsForLastday: result")
                        val formatter = SimpleDateFormat("dd.MM.YY ");
                        val dateString = formatter.format( Date(lastDayBegin.timeInMillis)) + ".txt";
                        writeDataToFile(createTxtFile(dateString), res)
                        lastDayBegin = getCurrentDayBegin()
        }
    }

    fun removeObservers() {
        repository.getPressuresInIntervalLive(lastDayBegin.timeInMillis,
        getCurrentDayEnd(lastDayBegin).timeInMillis).removeObservers(this)
    }

    /**
     * fun for test writing into file
     */
    fun testWriteFile() {
        getPressrsForLastday()
    }

    /**
     * check if we pass to next day
     */
    private fun checkNextDay(): Boolean {
        val res = Calendar.getInstance().after(getCurrentDayEnd(lastDayBegin))
        Log.d(TAG, "checkNextDay: " + res)
        Log.d(TAG, "curr date: " + getTimeFormat(Calendar.getInstance()) +
                " end date" + getTimeFormat(getCurrentDayEnd(lastDayBegin)) )
        return res
    }

    /**
     * writing data to file
     */
    private suspend fun writeDataToFile(fileWriter: FileWriter?, data: List<Pressure>) {
        if (data != null) {

            Log.d(TAG, "writeDataToFile: begin size=" + data.size)
            if (data.size > 0) {
                val initTime = data[0].time
                for (pressure in data) {
                    fileWriter!!.append(
                        getTimeInHours((pressure.time - initTime).toInt()).toString() +
                            " " + pressure.pressure + " " + pressure.rainPower + " " + "\n")
                }
                fileWriter!!.close()
            }
            Log.d(TAG, "writeDataToFile: close file")
        } else {
            Log.d(TAG, "writeDataToFile: null data")
        }
    }

    /**
     * creating a txt file on sdcard
     */
    private fun createTxtFile(fileName: String): FileWriter? {
        return try {
            // create a File object for the parent directory

            // create a File object for the parent directory
            val wallpaperDirectory = File("/sdcard/Pressure/")
            // have the object build the directory structure, if needed.
            // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs()
            val file = File(wallpaperDirectory,fileName)
            FileWriter(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private var locationCount = 0

//    private var isLocatRecieved = false
    
    private var locationsList = mutableListOf<LocationResult>()

    private var altList = mutableListOf<Float>()

    /**
     * fun to get an Location from gps
     */
//    @SuppressLint("MissingPermission")
//    fun getLocationNew() {
//        Log.d(TAG, "getLocationNew: start")
//
//        // creating request
//        locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(20 * 1000.toLong())
//                .setMaxWaitTime(20*2*1000.toLong())
//                .setFastestInterval(20 * 1000.toLong())
//                .setNumUpdates(4)
//
////        isLocatRecieved = false
//
//        // creating callback
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                super.onLocationResult(locationResult)
//                if (locationResult != null) {
//                    locationsList.add(locationResult)
//
//                    altList.add(locationResult.lastLocation.altitude.toFloat())
//
//                    if (locationCount > 2) {
//                        Log.d(TAG, "onLocationResult: ")
//                        val altListPerf = mutableListOf<Float>()
//
//                        for(altitute in altList) {
//                            Log.d(TAG, "onLocationResult: alt= " + altitute)
//                            if (altitute > 1.0f && (altitute-lastAlt) < 60.0f) {
//                                altListPerf.add(altitute)
//                            }
//                        }
//
//                        var sumAlt = 0.0f
//                        for (altitude in altListPerf) {
//                            sumAlt += altitude
//                        }
//
//                        val com = sumAlt/altListPerf.size.toFloat()
//
////                        isLocatRecieved = true
//
//                        lastAlt = com
//
//                        locationCount = 0
//                        fusedLocationClient.removeLocationUpdates(locationCallback)
//
//                        ioScope.launch {
////                            zipper.generateAltit(com)
////                            zipper.generObserv()
//                            registerListn()
//                        }
//                    }
//
//                    Log.d(TAG, "onLocationResult: count=" + locationCount)
//                    locationCount ++
//                    var tempalt = locationResult.lastLocation.altitude.toFloat()
//                    if (tempalt > 0.0f || locationCount > 5) {
//                        if (tempalt == 0.0f) {
//                            tempalt = lastAlt
//                            Log.d(TAG, "onLocationResult: old val")
//                        } else {
//                            Log.d(TAG, "onLocationResult: new val")
//                        }
//                    }
//
//                } else {
//                    Log.d(TAG, "onLocationResult: location is null")
//                }
//                //Location received
//            }
//
//            override fun onLocationAvailability(p0: LocationAvailability?) {
//                super.onLocationAvailability(p0)
//                Log.d(TAG, "onLocationAvailability: ")
//            }
//        }
//
//        // getting location request
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//    }

    private fun saveFakeLastLocationValue(): Float {
        Log.d(TAG, "saveFakeLastLocationValue: ")
        var alt = 0.0f
        if (isWorkTime()) {
            alt = WORK_ALT
            return alt
        } else if (isHomeTime()) {
            alt = HOME_ALT
            return alt
        } else {
            alt = 150.0f
            return alt
        }
//        ioScope.launch {
//            zipper.generateAltit(alt)
//            zipper.generObserv()
//        }
    }

}