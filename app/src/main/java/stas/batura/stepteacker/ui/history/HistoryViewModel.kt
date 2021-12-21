package stas.batura.stepteacker.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.logic.getStepsSequenceInDaysPeriod
import java.util.*

class HistoryViewModel @ViewModelInject constructor(private var repository: Repository) : ViewModel() {

    val daysHistory = repository.getStepsListForDays(Calendar.getInstance(), 7).asLiveData()

    val daysHistoryCount = repository.getStepsListForDays(Calendar.getInstance(), 7).map {
        getStepsSequenceInDaysPeriod(it)
    }.asLiveData()

}