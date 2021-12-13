package stas.batura.stepteacker.ui.graph

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import stas.batura.stepteacker.data.Repository

class GraphViewModel @ViewModelInject constructor(private var repository: Repository) : ViewModel() {

    // массив шагов за текущий день
    val stepsList = repository.getDaysList().asLiveData()


}