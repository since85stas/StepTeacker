package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps_table")
data class Step(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var dateInMillis: Long = 0L,

    var steps: Int = 0
)
{
}