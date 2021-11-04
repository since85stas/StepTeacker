package stas.batura.stepteacker.ui.today

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import stas.batura.stepteacker.data.Repository

class TodayFragment @ViewModelInject constructor(
    val repository: Repository
        ) : ViewModel() {

    val steps = repository.getDaySteps().asLiveData()

}