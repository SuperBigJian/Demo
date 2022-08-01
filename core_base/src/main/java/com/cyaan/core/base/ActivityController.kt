package com.cyaan.core.base

import android.app.Activity
import java.util.*

object ActivityController {

    private val mActivityStack = Stack<Activity>()

    fun addActivity(activity: Activity) {
        mActivityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
    }


    fun finishCurrent() {
        if (mActivityStack.isNotEmpty()) {
            val activity = mActivityStack.lastElement()
            finishActivity(activity)
        }
    }

    fun finishAllExcludeCurrent() {
        mActivityStack.filter { it.localClassName != topActivity().localClassName }.forEach {
            it.finish()
            mActivityStack.remove(it)
        }
    }

    fun finishActivityByName(activityName: String) {
        val iterator = mActivityStack.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.javaClass.simpleName == activityName) {
                iterator.remove()
                item.finish()
            }
        }
    }

    fun finishActivity(activity: Activity) {
        removeActivity(activity)
        activity.finish()
    }

    fun topActivity() = mActivityStack.lastElement()

    fun finishAll() {
        mActivityStack.forEach { it.finish() }
        mActivityStack.clear()
    }

    fun getActivityStackInfo() {
        val sb = StringBuilder()
        mActivityStack.forEach {
            sb.append(it.componentName).append("\n")
        }
    }

    fun getStackSize() = mActivityStack.size
}