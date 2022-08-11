package com.cyaan.module.stock

import android.os.Debug
import android.os.storage.StorageManager
import com.cyaan.core.common.BaseApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockApplication : BaseApplication() {
    override fun onCreate() {
        Debug.startMethodTracing("$filesDir/dmtrace.trace")
        super.onCreate()
    }

}