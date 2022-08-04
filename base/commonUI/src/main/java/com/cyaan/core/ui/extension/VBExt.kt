package com.cyaan.core.ui.extension

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Activity.inflateBinding(inflate: (LayoutInflater) -> T) = lazy {
    inflate(layoutInflater).apply { setContentView(root) }
}

fun <T : ViewBinding> Dialog.inflateBinding(inflate: (LayoutInflater) -> T) = lazy {
    inflate(layoutInflater).apply { setContentView(root) }
}