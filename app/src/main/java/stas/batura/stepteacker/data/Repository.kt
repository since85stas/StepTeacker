package stas.batura.stepteacker.data

import kotlinx.coroutines.flow.Flow
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.Step
import java.util.*

interface Repository {

    fun updateDaySteps(steps: Int, date: String)

    fun getDaySteps(dateString: String): Flow<Day>

    fun dropStepsTable()

    fun addNewSteps(steps: Int, date: Long)

    fun getDaysList(): Flow<List<Step>>

    fun getPrefsStepsLimit(): Flow<Int>

    fun setPrefsStepsLimit(limit: Int)

    fun getStepsListForDays(currentTime: Calendar, periodInDays: Int): Flow<List<List<Step>>>
}