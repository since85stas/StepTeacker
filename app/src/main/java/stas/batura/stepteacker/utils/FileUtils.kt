package stas.batura.stepteacker.utils

import stas.batura.stepteacker.logger.NUM_WIDTH
import timber.log.Timber
import java.io.File
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * return timestamp from filename, removing all after first dot
 */
fun getTimeFromFileName(fileName: String): String {
    return if (fileName.contains(".")) {
        val dotPos = fileName.replaceAfter(".", "")
        dotPos.dropLast(1)
    } else {
        fileName
    }
}

/**
 * get days number
 * @param compDate comparisane data in string
 * @param currentDate time from what we measure in millis
 * @param form date fromat
 */
fun daysAfterTime(compDate: String, currentDate: Long, form: String?): Long {
    try {
        val sdf = SimpleDateFormat(form, Locale.getDefault())
        val date = sdf.parse(compDate)
        val diff = currentDate - date.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    } catch (e: ParseException) {
        Timber.e("Error in parse logfile date:$compDate.log $e")
    }
    return 0L
}

/**
 * get all logs in dir
 * @param path dir path
 */
fun getLogfiles(path: String): Array<File>? {
    val dir = File(path)

    // filtering .log and .json
    return dir.listFiles { dir, name -> name.contains(".log") || name.contains(".json")}
}

/**
 * get file log name
 * @param subType sub type of file: "all","err"
 * @param ext file extension
 * @param dateForm format for date parsing
 */
fun generateFileName( subType: String, ext: String, dateForm: String): String {
    val fileNameTimeStamp = SimpleDateFormat(
        dateForm,
        Locale.getDefault()
    ).format(Date())
    return "$fileNameTimeStamp.$subType.$ext"
}

/**
 * trying to open file
 * @param path path to file
 * @param fileName log filename
 */
fun generateFile(path: String, fileName: String): File? {
    var file: File? = null
    file = File(path, fileName)
    return file
}

/**
 * generates string for log number in format: "000011", "001345"
 * @param writeNumber log write number
 */
fun getWriteNumber(writeNumber: Int): String {

    if(writeNumber > 999999) return "999999"
    else if (writeNumber < 0) return "000000"

    val num = writeNumber.toString().reversed()

    val q: Stack<String> = Stack()

    for (i in 0..NUM_WIDTH-1) {
        if (i < num.length) {
            q.add(num[i].toString())
        } else {
            q.add("0")
        }
    }
    val b = StringBuilder()
    while (q.size > 0) {
        b.append(q.pop())
    }
    return b.toString()
}