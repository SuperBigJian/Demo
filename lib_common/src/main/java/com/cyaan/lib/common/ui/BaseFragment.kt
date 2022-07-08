package com.cyaan.lib.common.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import timber.log.Timber

class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate: ${this.javaClass.simpleName}")
        initArgument(arguments)
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume: ${this.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy: ${this.javaClass.simpleName}")
    }

    open fun initArgument(bundle: Bundle?) {}
}