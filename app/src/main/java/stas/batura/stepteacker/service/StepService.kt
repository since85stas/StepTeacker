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
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import stas.batura.stepteacker.MainActivity
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.utils.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class StepService @Inject constructor(

): LifecycleService() {

    /**
     * Job allows us to cancel all coroutines started by this ViewModel.
     */
    private var serviceJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this Service.
     */
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val TAG = StepService::class.simpleName

    private val NOTIFICATION_ID = 21

    private val CHANNEL_ID = "PressCh"

    // interval between saves in seconds
    private val INTERVAL = 60L * 5

    private var timeCount: Long = -10L

    @Inject lateinit var sensorManager: SensorManager

    @Inject lateinit var repository: Repository

//    @Inject lateinit var repository: Repository

    // pressure sensor installed
    private var sensor: Sensor? = null

    private val fakeSteps: Flow<Int> = flow {
        for (i in 1..1000) {
            delay(5000) // pretend we are doing something useful here
            emit(i) // emit next value
        }
    }

    init {
        collectStepsNumber(fakeSteps)
    }

    /**
     * собирает данные о шагах из источника и сохраняет в БД
      */
    private fun collectStepsNumber(steps: Flow<Int>) {
        serviceScope.launch {
            steps.collect { steps ->
                Log.d(TAG, "collectStepsNumber: $steps")
                repository.updateDaySteps(steps = steps, getTimeFormatString(Calendar.getInstance()))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        startForeground(NOTIFICATION_ID, getNotification())
        Log.d(TAG, "onCreate: " + deviceSensors)
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
        this.ServiceBinder().isBind = true
        return ServiceBinder()
    }

    /**
     * unbinding service to activirty
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "servise is unbind " + intent.toString())
        this.ServiceBinder().isBind = false
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        serviceJob.cancel()
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
    inner class ServiceBinder : Binder() {

        var isBind: Boolean = false

        fun closeService() {
            this@StepService.stopService()
        }

        fun updateNotif() {
            this@StepService.updateNotification()
        }
    }

    /**
     * stopping service
     */
    fun stopService() {
        stopSelf()
    }



}