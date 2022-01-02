package stas.batura.stepteacker.logger

import android.annotation.SuppressLint
import android.util.Log
import stas.batura.stepteacker.utils.generateFile
import stas.batura.stepteacker.utils.generateFileName
import stas.batura.stepteacker.utils.getWriteNumber
import timber.log.Timber.DebugTree
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

enum class LOG_TYPE{
    FILE,
    OUT
}

private val LOG_TAG = FileLoggingTree::class.java.simpleName

// logs expiration days
const val DAYS_DEL_ALL = 14

// http logs expiration days
const val DAYS_DEL_HTTP = 7

// file date format
const val SAVE_FILE_FORM = "yyyy-MM-dd-HH"

//TODO: temporary log path
//const val TEMP_PATH =
//    "/storage/emulated/0/Android/data/com.liqvid.android_tv/files/SMB/logs"

// log date format
private const val SAVE_LOG_FORM = "yyyy-MM-dd HH:mm:ss.SSS"

// timeline log file date format
const val TIMELINE_TIME_FORM = "yyyy-MM-dd_HH-mm-ss.SSS"

// width of number field
const val NUM_WIDTH = 6

class FileLoggingTree(val path:String) : DebugTree() {

    // counter for all logs
    private var writeNumber: Int = 0

    // counter for error logs
    private var errNumber: Int = 0

    // counter for error logs
    private var httpNumber: Int = 0

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        writeNumber++
        try {
            var savedMessage = message
                val allFile =
                    generateFile(path, generateFileName("all", "log", SAVE_FILE_FORM))

                // save log to alllog file
                if (allFile != null) {
                    val writer = FileWriter(allFile, true)
                    val timeStamp = SimpleDateFormat(
                        SAVE_LOG_FORM,
                        Locale.getDefault()
                    ).format(Date())
                    addLogsToWriter(writer, timeStamp, priority, savedMessage, tag, writeNumber)
                    writer.flush()
                    writer.close()
                }

                // save log to errlog file
                if (priority == Log.ERROR) {
                    errNumber++
                    // Create file
                    val errFile =
                        generateFile(path, generateFileName("err", "log", SAVE_FILE_FORM))
                    if (errFile != null) {
                        val writer = FileWriter(errFile, true)
                        val timeStamp = SimpleDateFormat(
                            SAVE_LOG_FORM,
                            Locale.getDefault()
                        ).format(Date())
                        addLogsToWriter(writer, timeStamp, priority, savedMessage, tag, errNumber)
                        writer.flush()
                        writer.close()
                    }
                }

        } catch (e: Exception) {
            Log.e(
                LOG_TAG,
                "Error while logging into file : $e"
            )
        }
    }

    /**
     * generates string to adding in logs
     */
    private fun addLogsToWriter(
        writer: FileWriter,
        timeStamp: String,
        priority: Int,
        message: String,
        tag: String?,
        number: Int
    ): FileWriter {
        writer.append(timeStamp) //                        .append(tag)
            .append(" ")
            .append("|| ")
            .append(getWriteNumber(number))
            .append(" ")
            .append("|| ")
            .append(getPriorityString(priority))
            .append("-")
            .append(" ")
            .append(message)
            .append(" ")
            .append("|| ")
            .append(tag)
            .append("\n")
        return writer
    }

    /**
     * get save priority
     * @param priority priority
     */
    private fun getPriorityString(priority: Int): String {
        return when (priority) {
            Log.DEBUG -> "DEBUG"
            Log.ERROR -> "ERROR"
            Log.INFO -> "INFO"
            Log.WARN -> "WARN"
            else -> "UNKN"
        }
    }

    override fun log(priority: Int, message: String, vararg args: Any) {
        super.log(priority, message, *args)
    }

}