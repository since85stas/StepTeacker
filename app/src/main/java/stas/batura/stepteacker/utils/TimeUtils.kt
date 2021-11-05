package stas.batura.stepteacker.utils

import java.text.SimpleDateFormat
import java.util.*

const val TIME_FORMAT = "YYYY:MM:dd"

fun getTimeFormatString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(TIME_FORMAT);
    val dateString = formatter.format( Date(calendar.timeInMillis))
    return dateString
}