package com.cyaan.lib.business.view.ripple

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock

class RippleDrawable : Drawable(), Runnable {
    private var mRippleCount = 3
    private var mRippleDis = 50
    private val mRippleList by lazy { ArrayList<RippleData>(mRippleCount) }
    private var mIsRunning = false
    private val mDrawRect = RectF()
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mStartWidth = 0
    private var mStartHeight = 0
    private var mInited = false
    private val mPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private fun initView() {
        if (mInited) {
            return
        }
        for (i in 0 until mRippleCount) {
            val data = RippleData()
            data.width = mStartWidth + (mRippleDis * i)
            data.height = mStartHeight + (mRippleDis * i)
            data.x = (mViewWidth - data.width) / 2
            data.y = (mViewHeight - data.height) / 2
            mRippleList.add(data)
        }
    }

    override fun run() {
        unscheduleSelf(this)
        if (mIsRunning) {
            invalidateSelf()
            scheduleSelf(this, SystemClock.uptimeMillis() + 16)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(bounds)
        for (i in 0 until mRippleCount) {
            val ripple = mRippleList[i]
            mDrawRect.left = ripple.x.toFloat()
            mDrawRect.top = ripple.y.toFloat()
            mDrawRect.right = (ripple.x + ripple.width).toFloat()
            mDrawRect.bottom = (ripple.y + ripple.height).toFloat()
            if (ripple.width > mStartWidth) {
                val progress = (ripple.width - mStartWidth) / (mViewWidth - mStartHeight).toDouble()
                mPaint.alpha = (255 - progress * 200).toInt()
                canvas.drawOval(mDrawRect, mPaint)
                ripple.width += (5 * progress).toInt()
                ripple.height += (5 * progress).toInt()
                ripple.x = (mViewWidth - ripple.width) / 2
                ripple.y = (mViewHeight - ripple.height) / 2
            } else {
                ripple.width += 5
                ripple.height += 5
                ripple.x = (mViewWidth - ripple.width) / 2
                ripple.y = (mViewHeight - ripple.height) / 2
            }

            if (ripple.width >= mViewWidth || ripple.height >= mViewHeight) {
                //从零开始这样波纹之间会有间隔
                ripple.width = 0
                ripple.height = 0
            }
        }
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        if (right - left != mViewWidth || bottom - top != mViewHeight) {
            mViewWidth = right - left
            mViewHeight = bottom - top
            mInited = false
            initView()
        }
    }

    fun initPrimarySize(width: Int, height: Int) {
        mStartWidth = width
        mStartHeight = height
    }

    fun start() {
        stop()
        mIsRunning = true
        run()
    }

    fun stop() {
        if (!mIsRunning) return
        mIsRunning = false
        unscheduleSelf(this)
    }

    data class RippleData(
        var x: Int = 0,
        var y: Int = 0,
        var width: Int = 0,
        var height: Int = 0,
    )
}