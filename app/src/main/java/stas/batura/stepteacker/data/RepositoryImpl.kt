package stas.batura.stepteacker.data

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.StepsDao
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    val stepDao: StepsDao
): Repository {


    private var repositoryJob = Job()

    private val repScope = CoroutineScope(Dispatchers.IO + repositoryJob)

    override fun insertDay(day: Day) {
        repScope.launch {
            stepDao.insertDay(day = day)
        }
    }

    override fun updateDaySteps(steps: Int, date: String) {
        repScope.launch {
            val day = stepDao.getDay(date)
            if (day == null) {
                stepDao.insertDay(Day(date, steps))
            } else {
                stepDao.updateDaySteps(steps, date)
            }
        }
    }

    override fun getDaySteps(): Flow<Day> {
        return stepDao.getDaySteps()
    }
}