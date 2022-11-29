package com.cyaan.core.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    private val mBinding by lazy { BaseActivityBaseBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate: ${this.javaClass.simpleName}")
        mBinding.baseRootView.bindContentView(initView())
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initParam(intent)
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

    open fun setListener() {}

    protected fun finishAll() {
        ActivityController.finishAll()
    }

    fun showContent(view: View? = null) {
        mBinding.baseRootView.showContent(view)
    }

    fun showLoading() {
        mBinding.baseRootView.showLoading()
    }

    fun showEmpty(msgStr: String) {
        mBinding.baseRootView.showErrorDefault(R.mipmap.base_image_empty, msgStr)
    }

    fun showNoNetError() {
        mBinding.baseRootView.showNoNet()
    }

    fun showError(msgStr: String) {
        mBinding.baseRootView.showErrorDefault(R.mipmap.base_image_network_error, msgStr)
    }
}
