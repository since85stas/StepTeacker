package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pressure_table")
data class Pressure (

        var pressure: Float = 0.0F,

        var time: Long = 0,

        var rainPower: Int = 0,

        var altitude: Float = 0.0f,

        @PrimaryKey(autoGenerate = true)
        var pressureId: Long = 0L

)

{
}