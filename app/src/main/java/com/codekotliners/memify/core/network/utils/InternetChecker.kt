package com.codekotliners.memify.core.network.utils

object InternetChecker {
    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            return (ipProcess.waitFor() == 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
