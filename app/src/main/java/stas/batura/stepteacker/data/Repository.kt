package stas.batura.stepteacker.data

import kotlinx.coroutines.flow.Flow
import stas.batura.stepteacker.data.room.Day

interface Repository {

    fun updateDaySteps(steps: Int, date: String)

    fun getDaySteps(dateString: String): Flow<Day>

    fun dropStepsTable()
}