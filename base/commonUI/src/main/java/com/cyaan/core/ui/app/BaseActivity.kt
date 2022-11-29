package com.cyaan.core.ui.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cyaan.core.common.ActivityController
import com.cyaan.core.ui.R
import com.cyaan.core.ui.databinding.BaseActivityBaseBinding
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    private val mBinding by lazy { BaseActivityBaseBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate: ${this.javaClass.simpleName}")
        handleIntent(intent, false)
        mBinding.baseRootView.bindContentView(initView())
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent, true)
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume: ${this.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy: ${this.javaClass.simpleName}")
    }

    abstract fun initView(): View

    open fun handleIntent(intent: Intent?, isNew: Boolean) {}

    open fun setListener() {}

    fun showContent(view: View? = null) {
        mBinding.baseRootView.showContent(view)
    }

    fun showLoading() {
        mBinding.baseRootView.showLoadingView()
    }

    fun showEmpty(msgStr: String) {
        mBinding.baseRootView.showSampleErrorView(R.mipmap.base_image_empty, msgStr)
    }

    fun showError(msgStr: String) {
        mBinding.baseRootView.showSampleErrorView(R.mipmap.base_image_network_error, msgStr)
    }

    fun showNoNet() {
        mBinding.baseRootView.showNoInternetConnection()
    }

    protected fun finishAll() {
        ActivityController.finishAll()
    }
}
