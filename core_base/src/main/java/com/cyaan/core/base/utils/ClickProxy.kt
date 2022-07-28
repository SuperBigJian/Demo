package com.cyaan.core.base.utils

import android.os.SystemClock
import android.view.View

class ClickProxy(
    // 单点击事件
    private val click: ((view: View) -> Unit),
    // 拦截的重复点击事件
    private val again: ((view: View) -> Unit)? = null,
    // 拦截的无效点击事件
    private val invalid: ((view: View) -> Unit)? = null,
    //有效时间点击次数
    private val validHintSize: Int = 1,
    //允许点击事件有效的间隔时间
    private val validTimes: Long = 800L,
    //允许多次点击事件有效的间隔时间
    private val multiTimes: Long = 3000L
) : View.OnClickListener {
    private var hints = Array<Long>(validHintSize) { 0 }

    private var lastClick: Long = 0

    override fun onClick(v: View) {
        System.arraycopy(hints, if (validHintSize > 1) 1 else 0, hints, 0, validHintSize - 1)
        hints[validHintSize - 1] = SystemClock.uptimeMillis()
        if (hints.last() - hints.first() <= multiTimes) {
            if (hints.first() - lastClick >= validTimes) {
                click.invoke(v)
                lastClick = hints.last()
            } else {
                again?.invoke(v)
            }
        } else {
            invalid?.invoke(v)
        }
    }
}

fun View.OnClick(click: ((view: View) -> Unit)) {
    this.setOnClickListener(ClickProxy(click))
}
