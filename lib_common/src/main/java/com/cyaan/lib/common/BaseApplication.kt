package com.cyaan.lib.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.cyaan.lib.common.ui.ActivityController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.properties.Delegates

abstract class BaseApplication : Application() {

    companion object {
        var appContext: Context by Delegates.notNull()
            private set

        var scope: CoroutineScope by Delegates.notNull()
            private set
    }

    var mIsDebug = false

    override fun onCreate() {
        super.onCreate()
        set(this)
        initTimber()
        initApp()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    private fun initTimber() {
        if (mIsDebug) Timber.plant(Timber.DebugTree())
    }

    abstract fun initApp()

    private fun set(baseApplication: BaseApplication) {
        appContext = baseApplication
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
}