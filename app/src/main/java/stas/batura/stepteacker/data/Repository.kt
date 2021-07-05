package stas.batura.stepteacker.data

import kotlinx.coroutines.flow.Flow
import stas.batura.stepteacker.data.room.Day

interface Repository {
    fun insertDay(day: Day)

    fun updateDaySteps(steps: Int, date: String)

    fun getDaySteps(): Flow<Day>
}