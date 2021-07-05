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
import stas.batura.stepteacker.service.StepService
import stas.batura.stepteacker.utils.getCurrentDayEnd
import java.util.*

class MainViewModel @ViewModelInject constructor(val repository: Repository) : ViewModel() {

    private val TAG = MainViewModel::class.simpleName

    var serviceConnection: MutableLiveData<ServiceConnection?> = MutableLiveData()

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

    fun testSave() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.testWrite()
        }
    }

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

}