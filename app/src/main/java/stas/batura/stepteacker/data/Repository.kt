package stas.batura.stepteacker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import stas.batura.stepteacker.data.room.PressureDao
import stas.batura.stepteacker.data.room.Pressure
import stas.batura.stepteacker.data.room.Rain
import javax.inject.Inject

interface IRep: PressureDao, IDop {

}

interface IDop {
//    fun getRainPow(): Rain
}

class Repository @Inject constructor(): IRep {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var repositoryJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [repositoryJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val ioScope = CoroutineScope(Dispatchers.IO + repositoryJob)

    @Inject lateinit var pressureData: PressureDao

    override fun insertPressure(pressure: Pressure) {
        ioScope.launch {
            pressureData.insertPressure(pressure)
        }
    }

    override fun getPressuresLive(): LiveData<List<Pressure>> {
        return pressureData.getPressuresLive()
    }

    override fun getPressuresLiveDay(): LiveData<List<Pressure>> {
        return pressureData.getPressuresLiveDay()
    }

    override fun getPressures(): List<Pressure> {
        return pressureData.getPressures()
    }

    override fun getPressuresInIntervalLive(statTime: Long, endTime: Long): LiveData<List<Pressure>> {
        return pressureData.getPressuresInIntervalLive(statTime, endTime)
    }

    override fun getPressuresInInterval(statTime: Long, endTime: Long): List<Pressure> {
        return pressureData.getPressuresInInterval(statTime, endTime)
    }

    override fun setLastRainPower(power: Int) {
        ioScope.launch {
            pressureData.setLastRainPower(power)
        }
    }

    override fun getRainPower(): Rain {
        var result: Rain = Rain(0)
        runBlocking {
            ioScope.async {
                result = pressureData.getRainPower()
            }.await()
        }
        if (result == null) {
            val rain = Rain(0)
            insertRain(rain)
            return rain
        } else {
            return result
        }
    }

//    override fun getRainPow(): Rain {
//        val block = runBlocking {
//            getRainPow()
//        }
//        return block
//    }

//    override fun insertRain(rain: Rain) {
//        Log.d("ins","ins")
//    }

    override fun insertRain(rain: Rain) {
        ioScope.launch {
            pressureData.insertRain(rain)
        }
    }
//
//    override fun getRainList(): LiveData<List<Rain>> {
//        return pressureData.getRainList()
//    }
//
//    override fun lastRain(): LiveData<Rain?> {
//        return pressureData.lastRain()
//    }
}