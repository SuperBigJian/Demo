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
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.cyaan.core.common.extension.onClick
import com.cyaan.core.ui.R
import timber.log.Timber

class BaseRootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val defaultLayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private var mContentViewId: Int = R.id.base_root_content_view_id

    private var mLoadingLayoutId: Int = 0
    private var mError1LayoutId: Int = 0
    private var mError2LayoutId: Int = 0

    private val mInflater by lazy { LayoutInflater.from(getContext()) }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRootView, defStyleAttr, 0)
        mLoadingLayoutId = a.getResourceId(R.styleable.BaseRootView_loadingView, R.layout.base_layout_state_loading)
        mError1LayoutId = a.getResourceId(R.styleable.BaseRootView_errorView, R.layout.base_layout_state_error)
        mError2LayoutId = a.getResourceId(R.styleable.BaseRootView_noNetWorkView, R.layout.base_layout_state_error_with_button)
        a.recycle()
    }

    fun bindContentView(view: View) {
        if (contains(view)) return
        if (view.id == View.NO_ID) {
            view.id = R.id.base_root_content_view_id
        }
        mContentViewId = view.id
        try {
            addView(view, childCount, defaultLayoutParams)
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    fun bindStateView(loadingView: Int, error1View: Int, error2View: Int) {
        mLoadingLayoutId = loadingView
        mError1LayoutId = error1View
        mError2LayoutId = error2View
    }

    fun showContent(view: View? = null) {
        val curContentView = findViewById<View>(mContentViewId)
        if (view != null && view != curContentView) {
            clear(curContentView)
            bindContentView(view)
            Timber.i("replace ContentView ${curContentView?.transitionName}:${curContentView?.id} to ${view.transitionName}:${view.id}")
        }
        showViewById(mContentViewId)
    }

    fun showLoading(initialization: (view: View) -> Unit = {}, update: (view: View) -> Unit = {}) {
        var view = findViewById<View>(R.id.base_root_loading_view_id)
        if (view == null) {
            view = inflateView(mLoadingLayoutId)
            initialization(view)
            addView(view, childCount, defaultLayoutParams)
        }
        update(view)
        showViewById(R.id.base_root_loading_view_id)
    }

    fun showError1(initialization: (view: View) -> Unit = {}, update: (view: View) -> Unit = {}) {
        var view = findViewById<View>(R.id.base_root_error_view_id_1)
        if (view == null) {
            view = inflateView(mError1LayoutId)
            initialization(view)
            addView(view, childCount, defaultLayoutParams)
        }
        update(view)
        showViewById(R.id.base_root_error_view_id_1)
    }

    fun showError2(initialization: (view: View) -> Unit = {}, update: (view: View) -> Unit = { }) {
        var view = findViewById<View>(R.id.base_root_error_view_id_2)
        if (view == null) {
            view = inflateView(mError2LayoutId)
            initialization(view)
            addView(view, childCount, defaultLayoutParams)
        }
        update(view)
        showViewById(R.id.base_root_error_view_id_2)
    }

    fun showLoadingView(msgStr: String = "") {
        showLoading(update = {
            if (msgStr.isNotBlank()) {
                val msg = it.findViewById<TextView>(R.id.progressTv)
                msg.text = msgStr
            }
        })
    }

    fun showSampleErrorView(iconRes: Int, msgStr: String) {
        showError1(update = {
            val icon = it.findViewById<ImageView>(R.id.errorIv)
            val msg = it.findViewById<TextView>(R.id.errorTv)
            icon.setImageResource(iconRes)
            msg.text = msgStr
        })
    }

    fun showNoInternetConnection() {
        showError2(initialization = {
            val btn = it.findViewById<Button>(R.id.errorBtn)
            btn.onClick {
                ToastUtils.showShort("打开设置")
                context.startActivity(Intent().apply {
                    action = "android.settings.WIFI_SETTINGS"
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        })
    }

    private fun showViewById(id: Int) {
        post {
            val childCount = childCount
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                view.isVisible = view.id == id
            }
        }
    }

    private fun inflateView(layoutId: Int): View? {
        return try {
            mInflater.inflate(layoutId, null)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    private fun clear(vararg views: View?) {
        try {
            for (view in views) {
                view?.let { removeView(it) }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}