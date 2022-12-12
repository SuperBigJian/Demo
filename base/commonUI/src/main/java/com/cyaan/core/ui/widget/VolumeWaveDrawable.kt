package com.cyaan.core.ui.widget

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.SparseArray
import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

class VolumeWaveDrawable : Drawable(), Runnable {
    /**
     * 绘制时间间隔
     */
    private val DRAW_INTERVAL = 0.016f

    /**
     * 缓存buf的最小值 20*3 + 2
     */
    private val minBufferSize = 62

    private var mInited = false

    private var mLineWidth = 0f
    private var mStepWidth = 0
    private var mLineCount = 0

    private var mBaseWidth = 0f
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mMinHeight = 0

    private var mLowMode = 0
    private var mLineDataList: MutableList<LineData> = ArrayList()

    private lateinit var mSv: BooleanArray
    private lateinit var mEh: FloatArray
    private lateinit var mLoc: IntArray

    private val mPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = mNormalColor
        }
    }
    private var mIsRunning = false
    private var mMode = E_VOLUME
    private var mIndex = 0
    private var mIncrease = false
    private val mDrawRect: RectF = RectF()
    private var mDenominator = 0.0
    private val mRandomCaches = SparseArray<Double>()
    private val mHeightCaches = SparseArray<Double>()
    private var mNormalColor = -0xd06e02
    private val mDisableColor = -0x7fd06e02
    private var mEnable = true

    fun setMode(mode: Int): VolumeWaveDrawable {
        mMode = mode
        if (mode == E_WAIT) {
            mIndex = 0
            mIncrease = true
        }
        return this
    }

    fun setStepWidth(stepWidth: Int) {
        mStepWidth = stepWidth
    }

    fun setLineWidth(lineWidth: Int) {
        mBaseWidth = lineWidth.toFloat()
        mLineWidth = lineWidth.toFloat()
    }

    fun setMinHeight(minHeight: Int) {
        mMinHeight = minHeight
    }

    fun setNormalColor(colorRgb: Int) {
        mNormalColor = colorRgb
        mPaint.color = mNormalColor
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(bounds)
        if (mMode == E_VOLUME) {
            drawVolume(canvas)
        } else if (mMode == E_WAIT) {
            drawWait(canvas)
        }
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        if (right - left != mViewWidth || bottom - top != mViewHeight) {
            mViewWidth = right - left
            mViewHeight = bottom - top
            mInited = false
            initDrawable()
        }
    }

    private fun initDrawable() {
        if (mInited) {
            return
        }
        mLineCount = mViewWidth / (mLineWidth.toInt() + mStepWidth)
        mDenominator = mLineCount.toDouble().pow(4.0)
        var start = (mViewWidth - (mLineWidth.toInt() + mStepWidth) * mLineCount) / 2

        for (i in 0 until mLineCount) {
            val data = LineData()
            data.x = start
            data.y = mViewHeight / 2
            data.width = mLineWidth.toInt()
            data.height = mMinHeight
            data.timelist = ArrayList()
            mLineDataList.add(data)
            start += (mLineWidth + mStepWidth).toInt()
        }
        mSv = BooleanArray(mLineCount)
        var count = mLineCount
        if (count < minBufferSize) {
            count = minBufferSize
        }
        mEh = FloatArray(count)
        mLoc = IntArray(count)
        mInited = true
    }

    private fun getRandomValue(key: Int): Double {
        if (mRandomCaches.indexOfKey(key) >= 0) {
            return mRandomCaches[key]
        }
        val value = 18 * key.toDouble().pow(4.0)
        mRandomCaches.put(key, value)
        return value
    }

    private fun getHeightValue(key: Int): Double {
        if (mHeightCaches.indexOfKey(key) >= 0) {
            return mHeightCaches[key]
        }
        val value = 0.5.pow(key.toDouble())
        mHeightCaches.put(key, value)
        return value
    }

    fun disable() {
        if (mEnable) {
            mEnable = false
            initDrawable()
            mPaint.color = mDisableColor
            mLineDataList.forEach {
                it.height = mMinHeight
            }
        }
    }

    fun reset() {
        if (mMode != E_VOLUME) {
            return
        }
        initDrawable()
        if (!mEnable) {
            mEnable = true
            mPaint.color = mNormalColor
        }
        for (data in mLineDataList) {
            data.timelist?.clear()
            data.height = mMinHeight
        }
    }

    fun setVolume(volume: Int) {
        if (mMode != E_VOLUME) {
            return
        }
        initDrawable()
        if (!mEnable) {
            mEnable = true
            mPaint.color = mNormalColor
        }
        var input = (volume / 8.0).toFloat()
        if (input > 0.6f) {
            input = 0.6f
        }
        val half = (mLineCount / 2).toFloat()
        // 计算每一点是否出现
        var si = input
        if (si < 0.1f) {
            if (mLowMode in 1..2) {
                mLowMode++
                return
            }
            mLowMode = 0
            mLowMode++
            si = 0.1f
            input = 0.05f
        } else {
            input += 2.0f
            mLowMode = 0
        }
        for (i in 0 until mLineCount) {
            if (i < half) {
                mSv[i] = randomBool((si * (getRandomValue(i) / mDenominator + 0.05f) * 100).toInt())
            } else {
                mSv[i] = randomBool((si * (getRandomValue(i - mLineCount) / mDenominator + 0.05f) * 100).toInt())
            }
        }
        // 计算出波峰数
        var m = (20 * input).toInt() + getRandom() % 3 - 1
        m = if (m <= 0) 1 else m

        // 计算出高度
        val standH = input.toDouble().pow(0.333).toFloat() * mViewHeight * 0.8f
        for (i in 0 until m) {
            if (i < half) {
                mEh[i] = (standH * (getRandomValue(i) / mDenominator + 0.05f) * 10).toFloat()
            } else {
                mEh[i] = (standH * (getRandomValue(i - mLineCount) / mDenominator + 0.05f) * 10).toFloat()
            }
        }
        var loc = 0
        for (j in 0 until mLineCount) {
            if (mSv[j]) {
                mLoc[loc++] = j
            }
        }
        if (loc > 1) {
            val chc = 30.coerceAtMost(loc)
            for (i in 0 until chc) {
                val s1 = getRandom() % loc
                var s2 = getRandom() % loc
                if (s1 == s2) {
                    s2 = (s2 + 1) % loc
                }
                val t = mLoc[s1]
                mLoc[s1] = mLoc[s2]
                mLoc[s2] = t
            }
        }

        // 实际的波峰数
        val factm = m.coerceAtMost(loc)
        for (i in 0 until mLineCount) {
            var fh = mMinHeight.toFloat()
            for (l in 0 until factm) {
                fh += (mEh[l] * getHeightValue(abs(mLoc[l] - i))).toFloat()
            }
            if (fh > mViewHeight) {
                fh = mViewHeight.toFloat()
            }
            val line = mLineDataList[i]
            if (abs(fh - line.lastHeight) >= 1) {
                var t = 0.135f
                if (fh < line.lastHeight) {
                    t = 0.3f
                }
                val fu = FrameUnit()
                fu.c = fh - line.lastHeight
                fu.d = t
                fu.b = 0f
                fu.t = 0f
                line.timelist?.add(fu)
            }
            line.lastHeight = fh
        }
    }

    private fun drawVolume(canvas: Canvas) {
        val rect = bounds
        for (i in 0 until mLineCount) {
            val line = mLineDataList[i]
            val timelist = line.timelist ?: return
            var dy = 0f
            var j = 0
            while (j < timelist.size) {
                val fu = timelist[j]
                fu.t += DRAW_INTERVAL
                var onceDy: Float
                if (fu.t > fu.d) {
                    onceDy = fu.c - fu.b
                    timelist.removeAt(j)
                    j--
                } else {
                    onceDy = quartInOut(fu.t.toDouble(), 0.0, fu.c.toDouble(), fu.d.toDouble()).toFloat()
                    val last = fu.b
                    fu.b = onceDy
                    onceDy -= last
                }
                dy += onceDy
                j++
            }
            if (dy != 0f) {
                var lh = line.height + dy
                if (lh < mMinHeight) {
                    lh = mMinHeight.toFloat()
                }
                line.height = lh.toInt()
            } else if (timelist.size == 0) {
                val lh = line.height.toFloat()
                if (lh < mMinHeight) {
                    line.height = mMinHeight
                }
            }
            mDrawRect.left = (line.x + rect.left).toFloat()
            mDrawRect.top = (line.y - line.height / 2 + rect.top).toFloat()
            mDrawRect.right = mDrawRect.left + mLineWidth
            mDrawRect.bottom = mDrawRect.top + line.height
            canvas.drawRect(mDrawRect, mPaint)
        }
    }

    private fun drawWait(canvas: Canvas) {
        val rect = bounds
        if (mIncrease) {
            for (i in 0 until mLineCount) {
                val line = mLineDataList[i]
                mDrawRect.left = (line.x + rect.left).toFloat()
                mDrawRect.right = line.x + mLineWidth + rect.left
                when (i) {
                    mIndex - 1 -> {
                        mDrawRect.top = (line.y - mMinHeight * 3 / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight * 3
                    }
                    mIndex -> {
                        mDrawRect.top = (line.y - mMinHeight * 5 / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight * 5
                    }
                    else -> {
                        mDrawRect.top = (line.y - mMinHeight / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight
                    }
                }
                canvas.drawRoundRect(mDrawRect, 5f, 5f, mPaint)
            }
            mIndex += 2
            if (mIndex >= mLineCount) {
                mIndex = mLineCount - 1
                mIncrease = false
            }
        } else {
            for (i in 0 until mLineCount) {
                val line = mLineDataList[i]
                mDrawRect.left = (line.x + rect.left).toFloat()
                mDrawRect.right = line.x + mLineWidth + rect.left
                when (i) {
                    mIndex + 1 -> {
                        mDrawRect.top = (line.y - mMinHeight * 3 / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight * 3
                    }
                    mIndex -> {
                        mDrawRect.top = (line.y - mMinHeight * 5 / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight * 5
                    }
                    else -> {
                        mDrawRect.top = (line.y - mMinHeight / 2 + rect.top).toFloat()
                        mDrawRect.bottom = mDrawRect.top + mMinHeight
                    }
                }
                canvas.drawRoundRect(mDrawRect, 5f, 5f, mPaint)
            }
            mIndex -= 2
            if (mIndex < 0) {
                mIndex = 0
                mIncrease = true
            }
        }
    }

    override fun run() {
        unscheduleSelf(this)
        if (mIsRunning) {
            invalidateSelf()
            if (mMode == E_VOLUME) {
                scheduleSelf(this, SystemClock.uptimeMillis() + 16)
            } else if (mMode == E_WAIT) {
                scheduleSelf(this, SystemClock.uptimeMillis() + 50)
            }
        }
    }

    fun start() {
        stop()
        mIsRunning = true
        run()
    }

    fun stop() {
        if (!mIsRunning) return
        mIsRunning = false
        mIncrease = false
        mIndex = 0
        unscheduleSelf(this)
    }

    private class LineData {
        var x = 0
        var y = 0
        var width = 0
        var height = 0
        var lastHeight = 0f
        var timelist: MutableList<FrameUnit>? = null
    }

    private class FrameUnit {
        var c = 0f
        var d = 0f
        var b = 0f
        var t = 0f
    }

    private fun randomBool(percent: Int): Boolean {
        if (percent <= 0) {
            return false
        }
        return if (percent >= 100) {
            true
        } else getRandom() % 1000 < 10 * percent
    }

    private fun quartInOut(t: Double, b: Double, c: Double, d: Double): Double {
        return -c / 2 * (cos(Math.PI * t / d) - 1) + b
    }

    private val random: Random
        get() = Random(System.currentTimeMillis())

    private fun getRandom(): Int {
        return abs(random.nextInt())
    }

    companion object {
        const val E_VOLUME = 1
        const val E_WAIT = 2
    }
}