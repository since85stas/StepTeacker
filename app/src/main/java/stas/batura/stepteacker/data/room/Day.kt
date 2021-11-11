package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days_table")
data class Day (

        @PrimaryKey()
        var date: String = "",

        var steps: Int = 0

)

{
}