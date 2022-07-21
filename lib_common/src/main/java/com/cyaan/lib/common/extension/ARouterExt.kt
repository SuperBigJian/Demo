package com.cyaan.lib.common.extension

import android.os.Bundle
import androidx.core.os.bundleOf
import com.alibaba.android.arouter.launcher.ARouter

fun routeTo(path: String, block: (Bundle.() -> Unit) = {}) {
    val bundle = bundleOf()
    block(bundle)
    ARouter.getInstance().build(path).with(bundle).navigation()
}