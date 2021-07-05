package stas.batura.stepteacker.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Insert
    fun insertDay(day: Day)

    @Query("UPDATE days_table SET steps =:steps WHERE date = :date")
    fun updateDaySteps(steps: Int, date: String)

    @Query("SELECT * FROM days_table")
    fun getDaySteps(): Flow<Day>
}