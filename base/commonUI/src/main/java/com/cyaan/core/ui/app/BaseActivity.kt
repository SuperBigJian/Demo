package com.cyaan.core.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i(this.javaClass.simpleName)
        initParam(intent)
        initView()
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
        Timber.i(this.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i(this.javaClass.simpleName)
    }

    open fun initParam(intent: Intent?) {}

    open fun initView() {}

    open fun setListener() {}
}
