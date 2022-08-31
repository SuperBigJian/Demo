package com.cyaan.demo.binder.remote

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.SparseArray
import com.cyaan.demo.binder.ITestAidlInterface
import timber.log.Timber

class UserManager : Service() {

    override fun onCreate() {
        super.onCreate()
        Timber.i("UserManager onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("UserManager onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.i("UserManager onBind")
        return ServerImp()
    }

    override fun onDestroy() {
        Timber.i("UserManager onDestroy")
        super.onDestroy()
    }
}

class ServerImp : ITestAidlInterface.Stub() {
    private val users = SparseArray<UserInfo>().apply {
        put(1, UserInfo(1, "李火旺", 25))
        put(2, UserInfo(2, "张三", 19))
        put(3, UserInfo(3, "李四", 15))
        put(4, UserInfo(4, "大头", 27))
    }

    override fun getInfoById(id: Int, user: UserInfo?) {
        users.get(id)?.let { it ->
            user?.copyInfo(it)
        }
    }

    override fun getUserInfo(user: UserInfo?): UserInfo? {
        users.get(user?.id ?: -1)?.let {
            user?.copyInfo(it)
            return it
        }
        return null
    }

    override fun getUserInfo2(user: UserInfo?): UserInfo? {
        users.get(user?.id ?: -1)?.let {
            user?.copyInfo(it)
            return it
        }
        return null
    }
}