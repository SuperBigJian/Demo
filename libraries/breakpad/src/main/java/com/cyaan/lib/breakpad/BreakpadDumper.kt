package com.cyaan.lib.breakpad

object BreakpadDumper {
    init {
        System.loadLibrary("breakpad-core")
    }

    fun initBreakpad(path: String) {
        initBreakpadNative(path)
    }

    private external fun initBreakpadNative(path: String)
}