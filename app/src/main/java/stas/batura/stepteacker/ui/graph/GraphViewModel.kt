package stas.batura.stepteacker.ui.graph

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import stas.batura.stepteacker.data.Repository
import stas.batura.stepteacker.data.logic.getStepsSequenceInDay

class GraphViewModel @ViewModelInject constructor(private var repository: Repository) : ViewModel() {

    // массив шагов за текущий день
    val stepsList = repository.getDaysList()

    val stepsUniformList = stepsList.map { getStepsSequenceInDay(it) }.asLiveData()

}