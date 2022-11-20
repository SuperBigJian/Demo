package com.cyaan.core.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.properties.Delegates

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        set(this)
        Timber.plant(CustomDebugTree())
    }

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
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (BuildConfig.DEBUG || Log.isLoggable("Level", Log.DEBUG)) {
                super.log(priority, tag, message, t)
            }
        }

        override fun createStackElementTag(element: StackTraceElement): String {
            return "${element.fileName}:${element.lineNumber}"
        }
    }
}