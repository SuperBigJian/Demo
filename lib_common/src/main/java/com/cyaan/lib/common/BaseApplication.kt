package com.cyaan.lib.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
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
        initARouter()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    private fun initTimber() {
        if (mIsDebug) Timber.plant(Timber.DebugTree())
    }

    private fun initARouter() {
        if (mIsDebug) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }

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