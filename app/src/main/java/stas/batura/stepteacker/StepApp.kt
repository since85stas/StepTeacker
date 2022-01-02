package stas.batura.stepteacker

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import stas.batura.stepteacker.logger.FileLoggingTree
import stas.batura.stepteacker.logger.LOG_TYPE
import timber.log.Timber
import java.io.File

// определяем куда пишем логи, в файл или только в out
val logType = LOG_TYPE.FILE

@HiltAndroidApp
class StepApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val logsPath  = applicationContext.getExternalFilesDir(null)?.absolutePath ?:
        applicationContext.filesDir.absolutePath +
        "/logs/"
        val logs = File(logsPath)
        if (!logs.exists()) {
            val succ = logs.mkdirs()
            if (!succ) {
                Log.d("app", "creating log dir: false")
            }
        }
        initTimber(logsPath)
    }

    /**
     * logging initializing
     * @param path путь к папке с файлами логов
     */
    fun initTimber(path:String) {
            when(logType) {
                LOG_TYPE.FILE -> Timber.plant(FileLoggingTree(path))
                LOG_TYPE.OUT -> Timber.plant(Timber.DebugTree())
            }
    }
}