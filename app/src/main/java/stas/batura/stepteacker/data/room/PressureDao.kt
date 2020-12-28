package stas.batura.stepteacker.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PressureDao {

    @Insert
    fun insertPressure(pressure: Pressure)

    @Query("SELECT * FROM pressure_table ORDER BY pressureId")
    fun getPressuresLive(): LiveData<List<Pressure>>

    @Query("SELECT * FROM pressure_table ORDER BY pressureId DESC LIMIT 280")
    fun getPressuresLiveDay(): LiveData<List<Pressure>>

    @Query("SELECT * FROM pressure_table ORDER BY pressureId")
    fun getPressures(): List<Pressure>

    @Query ("SELECT * FROM pressure_table WHERE time BETWEEN :statTime AND :endTime ORDER BY pressureId")
    fun getPressuresInIntervalLive(statTime: Long, endTime: Long): LiveData<List<Pressure>>

    @Query ("SELECT * FROM pressure_table WHERE time BETWEEN :statTime AND :endTime ORDER BY pressureId")
    fun getPressuresInInterval(statTime: Long, endTime: Long): List<Pressure>

//    @Insert
//    fun insertRain(rain: Rain)
//
//    @Query("SELECT * FROM rain_table")
//    fun getRainList(): LiveData<List<Rain>>
//    @Query("SELECT *FROM rain_table ORDER BY _id DESC LIMIT 1 ")
//    fun lastRain(): LiveData<Rain?>

    @Query("UPDATE rain_table SET lastPowr= :power")
    fun setLastRainPower(power: Int)

    @Query("SELECT * FROM rain_table LIMIT 1")
    fun getRainPower(): Rain

    @Insert
    fun insertRain(rain: Rain)

}