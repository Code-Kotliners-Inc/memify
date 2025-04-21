package com.codekotliners.memify.core.logger

import android.util.Log
import com.google.firebase.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

object Logger {
    enum class Level {
        DEBUG,
        INFO,
        WARNING,
        ERROR,
    }

    fun initialize(isDebug: Boolean) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!isDebug)
    }

    fun log(
        level: Level,
        tag: String = "APP_LOG",
        message: String,
        exception: Throwable? = null,
    ) {
        if (BuildConfig.DEBUG) {
            when (level) {
                Level.DEBUG -> Log.d(tag, message)
                Level.INFO -> Log.i(tag, message)
                Level.WARNING -> Log.w(tag, message)
                Level.ERROR -> Log.e(tag, message, exception)
            }
        }

        when (level) {
            Level.ERROR -> {
                FirebaseCrashlytics.getInstance().log("$tag: $message")
                exception?.let { FirebaseCrashlytics.getInstance().recordException(it) }
            }
            else -> FirebaseCrashlytics.getInstance().log("[$level] $tag: $message")
        }
    }

    fun logInfo(message: String, tag: String = "APP_INFO") = log(Level.INFO, tag, message)

    fun logDebug(message: String, tag: String = "APP_DEBUG") = log(Level.DEBUG, tag, message)

    fun logWarning(message: String, tag: String = "APP_WARNING") = log(Level.WARNING, tag, message)

    fun logError(message: String, exception: Throwable? = null, tag: String = "APP_ERROR") =
        log(Level.ERROR, tag, message, exception)
}
