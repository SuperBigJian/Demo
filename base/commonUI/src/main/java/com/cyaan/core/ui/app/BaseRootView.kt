package com.cyaan.core.ui.app

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.contains
import com.blankj.utilcode.util.ToastUtils
import com.iflytek.autofly.lifeservice.R
import com.iflytek.autofly.lifeservice.common.extension.onClick
import timber.log.Timber

class BaseRootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val defaultLayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private var mErrorResId: Int = 0
    private var mLoadingResId: Int = 0
    private var mNoNetResId: Int = 0

    private val mInflater by lazy { LayoutInflater.from(getContext()) }

    private var mContentView: View? = null
    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var mNoNetView: View? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRootView, defStyleAttr, 0)
        mLoadingResId = a.getResourceId(R.styleable.BaseRootView_loadingView, R.layout.base_layout_state_loading)
        mErrorResId = a.getResourceId(R.styleable.BaseRootView_errorView, R.layout.base_layout_state_error)
        mNoNetResId = a.getResourceId(R.styleable.BaseRootView_noNetWorkView, R.layout.base_layout_state_no_net)
        a.recycle()
    }

    fun bindStateView(errorView: Int, loadingView: Int, noNetView: Int) {
        mErrorResId = errorView
        mLoadingResId = loadingView
        mNoNetResId = noNetView
    }

    fun bindContentView(view: View) {
        if (contains(view)) return
        mContentView = view
        addView(mContentView, childCount, defaultLayoutParams)
    }

    fun showContent(view: View? = mContentView) {
        if (view != null && view != mContentView) {
            clear(mContentView)
            addView(mContentView, 0, defaultLayoutParams)
            Timber.i("replace ContentView ${mContentView?.transitionName}:${mContentView?.id} to ${view.transitionName}:${view.id}")
            mContentView = view
        }
        mContentView?.let {
            showViewAndHideOther(it)
        } ?: Timber.e(NoSuchElementException("Please [bindContentView] before show."))
    }

    fun showLoading() {
        if (mLoadingView == null) {
            mLoadingView = inflateView(mLoadingResId)
            addView(mLoadingView, 0, defaultLayoutParams)
        }
        mLoadingView?.let {
            showViewAndHideOther(it)
        }
    }

    fun showErrorDefault(iconRes: Int, msgStr: String) {
        showError {
            val icon = it.findViewById<ImageView>(R.id.errorIv)
            val msg = it.findViewById<TextView>(R.id.errorTv)
            icon.setImageResource(iconRes)
            msg.text = msgStr
        }
    }

    fun showError(initView: (view: View) -> Unit = {}) {
        if (mErrorView == null) {
            mErrorView = inflateView(mErrorResId)
            addView(mErrorView, 0, defaultLayoutParams)
        }
        mErrorView?.let {
            initView(it)
            showViewAndHideOther(it)
        }
    }

    fun showNoNet() {
        if (mNoNetView == null) {
            mNoNetView = inflateView(mNoNetResId)
            val btn = mNoNetView?.findViewById<Button>(R.id.networkBtn)
            btn?.onClick {
                ToastUtils.showShort("打开设置")
                context.startActivity(Intent().apply {
                    action = "android.settings.WIFI_SETTINGS"
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            addView(mNoNetView, 0, defaultLayoutParams)
        }
        mNoNetView?.let {
            showViewAndHideOther(it)
        }
    }

    private fun showViewAndHideOther(showView: View) {
        post {
            var result = false
            val childCount = childCount
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                view.visibility = if (view.id == showView.id) {
                    result = true
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            if (!result) Timber.e(IllegalStateException("showViewAndHideOther ${showView.transitionName}:${showView.id} haven`t add."))
        }
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater.inflate(layoutId, null)
    }

    private fun clear(vararg views: View?) {
        try {
            for (view in views) {
                view?.let { removeView(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}