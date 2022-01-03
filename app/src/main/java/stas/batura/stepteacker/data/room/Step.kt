package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

private const val STEP_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

@Entity(tableName = "steps_table")
data class Step(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var dateInMillis: Long = 0L,

    var steps: Int = 0
)
{

    fun getDateInNormalFormat(): String {
        val fileNameTimeStamp = SimpleDateFormat(
            STEP_TIME_FORMAT,
            Locale.getDefault()
        ).format(Date())
        return fileNameTimeStamp
    }

}