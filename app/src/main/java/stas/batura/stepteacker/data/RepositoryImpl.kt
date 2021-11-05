package stas.batura.stepteacker.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.RoomI
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    val stepDao: RoomI
): Repository {

    private var stepsLastSavedValue = 0

    private var repositoryJob = Job()

    private val repScope = CoroutineScope(Dispatchers.IO + repositoryJob)

    /**
     * обновляем данные о дне
     */
    override fun updateDaySteps(newSteps: Int, date: String) {
        repScope.launch {
            val day = stepDao.getDay(date)
            if (day == null) {
                stepDao.insertDay(Day(date, newSteps))
            } else {
                val oldSteps = day.steps

                if (newSteps > oldSteps) {
                    stepDao.updateDaySteps(newSteps, date)
                    stepsLastSavedValue = 0
                } else {
                    if (stepsLastSavedValue == 0) stepsLastSavedValue = oldSteps
                    stepDao.updateDaySteps(stepsLastSavedValue + newSteps, date)
                }
            }
        }
    }

    /**
     * получаем данные о дне на конкретную дату
     */
    override fun getDaySteps(dateString: String): Flow<Day> {
        return stepDao.getDaySteps(dateString)
    }

    override fun dropStepsTable() {
        stepDao.dropTable()
    }
}