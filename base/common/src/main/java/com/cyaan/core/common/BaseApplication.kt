package com.cyaan.core.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import timber.log.Timber
import kotlin.system.measureTimeMillis

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        set(this)
        Timber.plant(CustomDebugTree())
        Timber.d("${this.javaClass.simpleName} onCreate")
        val time = measureTimeMillis {
            initHandler()
        }
        if (time > 300) {
            Timber.e("init time $time is to long please move not important sdk init to [IdleInitSDK]")
        }
        Looper.myQueue().addIdleHandler {
            IdleInitHandler()
            false
        }
    }

    protected open fun initHandler() {}

    protected open fun IdleInitHandler() {}

    private fun set(baseApplication: BaseApplication) {
        baseApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityController.removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityController.addActivity(activity)
            }
        })
    }

    class CustomDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "${element.fileName}:${element.lineNumber}"
        }
    }
}