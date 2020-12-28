package stas.batura.stepteacker.utils

import java.text.SimpleDateFormat
import java.util.*

val MORN_START = 7
val MORN_END = 9

val EVN_START = 17
val EVN_END = 19

fun isWritingOn(): Boolean {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    if (hour in MORN_START until MORN_END) {
        return false
    } else if (hour in EVN_START until EVN_END) {
        return false
    } else {
        return true
    }
}

fun isWorkTime(): Boolean {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    if (hour in MORN_END until EVN_START) {
        return true
    } else {
        return false
    }
}

fun isHomeTime(): Boolean {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    if (hour in 0 until MORN_START) {
        return true
    } else if (hour in EVN_END..23){
        return true
    } else {
        return false
    }
}

fun getCurrentDayBegin(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 10)
    return calendar
}

fun getCurrentDayEnd(): Long {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 30)
    return calendar.timeInMillis
}

fun getCurrentDayEnd(calendar: Calendar): Calendar {
    var calendarNew: Calendar = calendar.clone() as Calendar
    calendarNew.set(Calendar.HOUR_OF_DAY, 23)
    calendarNew.set(Calendar.MINUTE, 59)
    calendarNew.set(Calendar.SECOND, 50)
    return calendarNew
}

fun getTimeInHours(time: Int): Float {
    val timeMin = time / (1000.0*60.0)
    val timeHours = timeMin
    return timeHours.toFloat()
}

fun getTimeFormat(calendar: Calendar): String {
    val formatter = SimpleDateFormat("dd/MM HH:mm");
    val dateString = formatter.format( Date(calendar.timeInMillis))
    return dateString
}