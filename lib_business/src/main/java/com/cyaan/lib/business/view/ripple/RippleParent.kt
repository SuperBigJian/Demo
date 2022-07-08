package com.cyaan.lib.business.view.ripple

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class RippleParent @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val rippleDrawable = RippleDrawable()

    private var rippleRatio = 2f

    init {
        background = rippleDrawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取宽高的测量模式以及测量值
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //测量所有子View
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else {
            if (childCount > 1) {
                throw IllegalStateException("RippleParent can host only one direct child")
            }
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                val childOne = getChildAt(0)
                val childWidth = childOne.measuredWidth
                val childHeight = childOne.measuredHeight
                //View的宽度=单个子View宽度*子View个数，View的高度=子View高度
                setMeasuredDimension(
                    (childWidth * rippleRatio).toInt(),
                    (childHeight * rippleRatio).toInt()
                )
            } else if (widthMode == MeasureSpec.AT_MOST) {
                val childOne = getChildAt(0)
                val childWidth = childOne.measuredWidth
                //View的宽度=单个子View宽度*子View个数，View的高度=xml当中设置的高度
                setMeasuredDimension((childWidth * rippleRatio).toInt(), heightSize)
            } else if (heightMode == MeasureSpec.AT_MOST) {
                val childOne = getChildAt(0)
                val childHeight = childOne.measuredHeight
                //View的宽度=xml当中设置的宽度，View的高度=子View高度
                setMeasuredDimension(widthSize, (childHeight * rippleRatio).toInt())
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        var child: View
        for (i in 0 until childCount) {
            child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                val childLeft = (measuredWidth - childWidth) / 2
                val childTop = (measuredHeight - childHeight) / 2
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
                rippleDrawable.initPrimarySize(childWidth + 20, childHeight + 20)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        rippleDrawable.start()
    }
}