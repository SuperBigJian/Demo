package com.cyaan.core.ui.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import timber.log.Timber

class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i(this.javaClass.simpleName)
        initArgument(arguments)
    }

    override fun onResume() {
        super.onResume()
        Timber.i(this.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i(this.javaClass.simpleName)
    }

    open fun initArgument(bundle: Bundle?) {}
}