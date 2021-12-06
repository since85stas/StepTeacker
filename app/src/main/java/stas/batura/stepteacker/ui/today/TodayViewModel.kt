package stas.batura.stepteacker.ui.today

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.logic.getStepsInDay
import stas.batura.stepteacker.utils.getTimeFormatString
import java.util.*

class TodayViewModel @ViewModelInject constructor(
    val repository: Repository
        ) : ViewModel() {

    val days = repository.getDaySteps(getTimeFormatString(Calendar.getInstance())).asLiveData()

    private val dayhistory = repository.getDaysList(getTimeFormatString(Calendar.getInstance()))

    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    val stepsForDayFromList = dayhistory.map {
        getStepsInDay(it)
    }.asLiveData()

    fun dropStepsTabale() {
        repository.dropStepsTable()
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}