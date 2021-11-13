package stas.batura.stepteacker.data

import kotlinx.coroutines.flow.Flow
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.Step

interface Repository {

    fun updateDaySteps(steps: Int, date: String)

    fun getDaySteps(dateString: String): Flow<Day>

    fun dropStepsTable()

    fun addNewSteps(steps: Int, date: Long)

    fun getDaysList(date: String): Flow<List<Step>>
}