package stas.batura.stepteacker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "params_table")
data class CommonParams (

    @PrimaryKey
    val id: Int,

    var currentDay: String,

    var dayLimit: Int

)