package stas.batura.stepteacker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Database: DaysDao, ParamsDao, StepsDao

@Dao
interface DaysDao {

    @Insert
    fun insertDay(day: Day)

    @Query("UPDATE days_table SET steps =:steps WHERE date = :date")
    fun updateDaySteps(steps: Int, date: String)

    @Query("SELECT * FROM days_table")
    fun getDaySteps(): Flow<Day>

    @Query("SELECT * FROM days_table WHERE date =:day")
    fun getDaySteps(day: String): Flow<Day>

    @Query("SELECT * FROM days_table WHERE date =:day")
    fun getDay(day: String): Day?

    @Query("DELETE FROM days_table")
    fun dropTable()



}

@Dao
interface StepsDao {

    @Insert
    fun insertStepNum(step: Step)

    @Query("SELECT * FROM steps_table")
    fun getAllSteps(): Flow<List<Step>>

    @Query("SELECT * FROM steps_table WHERE dateInMillis BETWEEN :dayStartTime AND :dayEndTime ORDER BY id")
    fun getStepsFortimeInterval(dayStartTime: Long, dayEndTime: Long): Flow<List<Step>>
}

@Dao
interface ParamsDao {

    @Insert
    fun insertParams(params: CommonParams)

    @Query("UPDATE params_table SET currentDay=:date WHERE id=0")
    fun updateCurrentDay(date: String)

    @Query("SELECT * FROM params_table WHERE id=0")
    suspend fun getStepsByDay(): CommonParams
}