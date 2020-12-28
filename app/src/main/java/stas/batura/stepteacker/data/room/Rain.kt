package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rain_table")
data class Rain (

        var lastPowr: Int = 0,

        @PrimaryKey
        var _id: Long = 0L

)