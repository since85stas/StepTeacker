package stas.batura.stepteacker.data

import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import stas.batura.stepteacker.data.pref.PreferencesScheme
import stas.batura.stepteacker.data.room.Day
import stas.batura.stepteacker.data.room.Database
import stas.batura.stepteacker.data.room.Step
import stas.batura.stepteacker.utils.getCurrentDayBegin
import stas.batura.stepteacker.utils.getCurrentDayEnd
import stas.batura.stepteacker.utils.getDayBegin
import stas.batura.stepteacker.utils.getPreviousDayBegin
import timber.log.Timber
import java.util.*
import javax.inject.Inject

// интервал через который проверяем обновление количества шагов
const val STEPS_CHECK_TIME_INTERVAL = 60000L

class RepositoryImpl @Inject constructor(
    val roomDao: Database,
    val prefs: DataStore<Preferences>
) : Repository {

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
            Timber.d("after step added steps:$steps saveTime: $date currtime: ${System.currentTimeMillis()}")
        }
    }


    override fun getDaysList(): Flow<List<Step>> {

        return flow {
            while (true) {
                val calDayBegin = getCurrentDayBegin()
                Log.d("TAG", "getDaysList: emit")
                emit(
                    roomDao.getStepsFortimeInterval(
                        calDayBegin.timeInMillis,
                        getCurrentDayEnd(calDayBegin).timeInMillis
                    )
                )
                delay(STEPS_CHECK_TIME_INTERVAL)
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

    override fun getPrefsStepsLimit(): Flow<Int> {
        return prefs.data.map { it ->
            val limit = it[PreferencesScheme.FIELD_STEP_LIMIT]
            Timber.d("limit from prefs: $limit")
            limit    ?: 10000
        }
    }

    /**
     * сохраняем лимит в шагах на день в настройках
     */
    override fun setPrefsStepsLimit(limit: Int) {
        repScope.launch {
            prefs.edit {
                it[PreferencesScheme.FIELD_STEP_LIMIT] = limit
            }
        }
    }

    /**
     * Получаем список записей о количестве шагов, от какого-то времени за кол-во дней
     * @param Calendar - время от которого будем считать шаги, по нему вычисляется начало дня
     * @param periodInDays - количество дней которые берм назад от дня опредленного в currentTime
     */
    override fun getStepsListForDays(currentTime: Calendar, periodInDays: Int): Flow<List<List<Step>>> {

        return flow {
            var dayBegin = getDayBegin(currentTime)

            val daysList = mutableListOf<List<Step>>()

            for (i in 0 until periodInDays) {
                val daySteps = roomDao.getStepsFortimeInterval(
                    getDayBegin(dayBegin).timeInMillis,
                    getCurrentDayEnd(dayBegin).timeInMillis
                )
                daysList.add(daySteps)

                dayBegin = getPreviousDayBegin(dayBegin)
            }
            emit(daysList)
        }
    }
}