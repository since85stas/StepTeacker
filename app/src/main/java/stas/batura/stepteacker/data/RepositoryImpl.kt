package stas.batura.stepteacker.data

import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import stas.batura.stepteacker.data.pref.PreferencesScheme
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.Database
import stas.batura.stepteacker.data.room.Step
import stas.batura.stepteacker.utils.getCurrentDayBegin
import stas.batura.stepteacker.utils.getCurrentDayEnd
import javax.inject.Inject

// интервал через который проверяем обновление количества шагов
const val STEPS_CHECK_TIME_INTERVAL = 5000L

class RepositoryImpl @Inject constructor(
    val roomDao: Database,
    val prefs: DataStore<Preferences>
): Repository {

    private var stepsLastSavedValue = 0

    private var repositoryJob = Job()

    private val repScope = CoroutineScope(Dispatchers.IO + repositoryJob)

    /**
     * обновляем данные о дне - сохраняем количество шагов за этот день.
     */
    override fun updateDaySteps(newSteps: Int, date: String) {
        repScope.launch {
            val day = roomDao.getDay(date)
            if (day == null) {
                roomDao.insertDay(Day(date, newSteps))
            } else {
                val oldSteps = day.steps

                if (newSteps > oldSteps) {
                    roomDao.updateDaySteps(newSteps, date)
                    stepsLastSavedValue = 0
                } else {
                    if (stepsLastSavedValue == 0) stepsLastSavedValue = oldSteps
                    roomDao.updateDaySteps(stepsLastSavedValue + newSteps, date)
                }
            }
        }
    }

    override fun addNewSteps(steps: Int, date: Long) {
        repScope.launch {
            roomDao.insertStepNum(
                Step(
                    steps = steps,
                    dateInMillis = date
                )
            )
        }
    }


    override fun getDaysList(date: String): Flow<List<Step>> {

        return flow {
            while (true) {
                val calDayBegin = getCurrentDayBegin()
                kotlinx.coroutines.delay(STEPS_CHECK_TIME_INTERVAL)
                Log.d("TAG", "getDaysList: emit")
                emit (roomDao.getStepsFortimeInterval(
                    calDayBegin.timeInMillis,
                    getCurrentDayEnd(calDayBegin).timeInMillis
                ))
            }
        }
    }

    /**
     * получаем данные о дне на конкретную дату
     */
    override fun getDaySteps(dateString: String): Flow<Day> {
        return roomDao.getDaySteps(dateString)
    }

    /**
     * очищаем данные о шагах
     */
    override fun dropStepsTable() {
        repScope.launch {
            roomDao.dropTable()
        }
    }

    fun getPrefsStepsLimit(): Flow<Int> {
        return prefs.data.map {it->
                it[PreferencesScheme.FIELD_STEP_LIMIT] ?: 0
        }
    }
}