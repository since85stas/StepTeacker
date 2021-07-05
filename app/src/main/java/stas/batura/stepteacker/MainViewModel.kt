package stas.batura.stepteacker

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.room.Step
import stas.batura.stepteacker.service.StepService
import stas.batura.stepteacker.utils.getCurrentDayEnd
import java.util.*

class MainViewModel @ViewModelInject constructor(val repository: Repository) : ViewModel() {

    private val TAG = MainViewModel::class.simpleName

    var serviceConnection: MutableLiveData<ServiceConnection?> = MutableLiveData()

    var lastDayPressures: MutableLiveData<List<Step>?> = MutableLiveData(null)

    private var playerServiceBinder: StepService.PressureServiceBinder? = null

    private var _stopServiceLive: MutableLiveData<Boolean> = MutableLiveData(false)
    val stopServiceLive: LiveData<Boolean>
        get() = _stopServiceLive

    init {
//        createService()
    }

    /**
     * creating our service and getting connection
     */
    fun createService() {

        if (serviceConnection.value == null) {

            // соединение с сервисом
            serviceConnection.value = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    try {
                        playerServiceBinder = service as StepService.PressureServiceBinder

                        updateNotif()
                    } catch (e: RemoteException) {
                        Log.d(TAG, "onServiceConnected: " + e);
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    playerServiceBinder = null
                }
            }

        }

    }

    /**
     * closing service through a Binder, after we unbinding it
     */
    fun closeService() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.closeService()
        }
        _stopServiceLive.value = false
    }

    /**
     * sending command to unbind and close service
     */
    fun stopService() {
        _stopServiceLive.value = true
    }

    fun setServiceRain(rainp: Int) {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.setRainPower(rainp)
        }
    }

    /**
     * saving value in DB
     */
    fun savePressureValue() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.savePressure()
        }
    }

    fun combibeData() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.savePressure()
        }
    }

    fun getLastDayData(): Calendar? {
        if (playerServiceBinder != null) {
            return playerServiceBinder!!.getLastDayBeg()
        }
        return null
    }

    fun testSave() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.testWrite()
        }
    }

//    fun testLoc() {
//        if (playerServiceBinder != null) {
//            playerServiceBinder!!.testLocation()
//        }
//    }

    fun testRx() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.testRx()
        }
    }

    fun updateNotif() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.updateNotif()
        }
    }

    fun getDayPressuresValue() {
        val day = getLastDayData()
        if (day != null) {
            val pressures = repository.getPressuresInInterval(day.timeInMillis,
                    getCurrentDayEnd(day).timeInMillis)
            lastDayPressures.value = pressures
        }
    }

    fun crash() {
        throw RuntimeException("Test Crash");
    }
}