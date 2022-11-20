package com.cyaan.core.common.extension

import android.os.Handler
import android.os.Looper

class MainHandler {
    private val handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) = handler.post(runnable)

    fun postDelayed(runnable: Runnable, millis: Long) = handler.postDelayed(runnable, millis)

    fun removeCallbacks(runnable: Runnable) = handler.removeCallbacks(runnable)
}