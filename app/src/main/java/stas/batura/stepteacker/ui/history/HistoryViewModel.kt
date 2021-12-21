package stas.batura.stepteacker.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import stas.batura.stepteacker.data.Repository
import java.util.*

class HistoryViewModel @ViewModelInject constructor(private var repository: Repository) : ViewModel() {

    val daysHistory = repository.getStepsListForDays(Calendar.getInstance(), 7).asLiveData()

}