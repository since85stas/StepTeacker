package stas.batura.stepteacker.utils

import java.text.SimpleDateFormat
import java.util.*

const val TIME_FORMAT = "YYYY:MM:dd"

fun getTimeFormatString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(TIME_FORMAT);
    val dateString = formatter.format( Date(calendar.timeInMillis))
    return dateString
}

fun getCurrentDayBegin(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 10)
    return calendar
}

fun getCurrentDayEnd(calendar: Calendar): Calendar {
    var calendarNew: Calendar = calendar.clone() as Calendar
    calendarNew.set(Calendar.HOUR_OF_DAY, 23)
    calendarNew.set(Calendar.MINUTE, 59)
    calendarNew.set(Calendar.SECOND, 50)
    return calendarNew
}