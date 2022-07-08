package com.cyaan.lib.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import timber.log.Timber
import java.lang.reflect.ParameterizedType

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate: ${this.javaClass.simpleName}")
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
        Timber.i("onResume: ${this.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy: ${this.javaClass.simpleName}")
    }

    open fun initParam(intent: Intent?) {}

    open fun initView() {}

    open fun setListener() {}
}
