package com.cyaan.lib.common.extension

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.cyaan.lib.common.BaseApplication

val Int.colorCompat
    get() = ResourcesCompat.getColor(BaseApplication.appContext.resources, this, null)

val Int.toDrawableCompat: Drawable?
    get() = BaseApplication.appContext.resources.getDrawable(this, null)

val Int.toStringCompat
    get() = BaseApplication.appContext.resources.getString(this)