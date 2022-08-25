package com.cyaan.demo.breakpad

object CrashLib {
    init {
        System.loadLibrary("crash-lib")
    }

    external fun crashDump()
}