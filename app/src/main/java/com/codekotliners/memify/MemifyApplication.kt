package com.codekotliners.memify

import android.app.Application
import com.vk.id.VKID
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MemifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        VKID.init(this)
        VKID.instance.setLocale(Locale("ru"))
    }
}
