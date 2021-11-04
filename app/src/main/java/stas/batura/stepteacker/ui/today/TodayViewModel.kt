package stas.batura.stepteacker.ui.today

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import stas.batura.stepteacker.data.Repository

class TodayViewModel @ViewModelInject constructor(
    val repository: Repository
        ) : ViewModel() {

    val days = repository.getDaySteps().asLiveData()

}