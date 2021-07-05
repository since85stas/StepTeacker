package stas.batura.stepteacker.ui.graph

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import stas.batura.stepteacker.data.Repository

class GraphViewModel @ViewModelInject constructor(private var repository: Repository) : ViewModel() {

//    val pressList = repository.getPressures()
    val pressList = repository.getPressuresLiveDay()

    var lastPress = repository.getRainPower()

    fun saveRainPower(power: Int) {
        repository.setLastRainPower(power)
    }

    fun updateRainpower() {
        lastPress = repository.getRainPower()
    }
}