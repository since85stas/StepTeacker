package stas.batura.stepteacker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import stas.batura.stepteacker.logger.FileLoggingTree
import stas.batura.stepteacker.logger.LOG_TYPE
import timber.log.Timber

@HiltAndroidApp
class StepApp: Application() {

    init {

    }

    /**
     * logging initializing
     */
    fun initTimber(path:String) {
        if (BuildConfig.DEBUG) {
            when(logType) {
                LOG_TYPE.FILE -> Timber.plant(FileLoggingTree(path)
                LOG_TYPE.OUT -> Timber.plant(Timber.DebugTree())
            }
        }
    }
}