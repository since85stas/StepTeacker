package stas.batura.stepteacker.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import stas.batura.stepteacker.data.IRep

class MainFragmentViewModel @ViewModelInject constructor(val repository: IRep) : ViewModel() {

    val pressureLive = repository.getPressuresLiveDay()

    val lastPress = repository.getRainPower()

//    val lastPower = repository.getRainPower()

    fun saveRainPower(power: Int) {
        repository.setLastRainPower(power)
    }


}