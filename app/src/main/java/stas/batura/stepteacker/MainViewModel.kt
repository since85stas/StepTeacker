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

class MainViewModel @ViewModelInject constructor(val repository: Repository) : ViewModel() {

    private val TAG = MainViewModel::class.simpleName

    var serviceConnection: MutableLiveData<ServiceConnection?> = MutableLiveData()

    private var playerServiceBinder: StepService.ServiceBinder? = null

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
                        playerServiceBinder = service as StepService.ServiceBinder

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
    }

    fun updateNotif() {
        if (playerServiceBinder != null) {
            playerServiceBinder!!.updateNotif()
        }
    }

}