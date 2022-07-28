package com.cyaan.core.ui.extension

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.cyaan.core.ui.app.BaseApplication

val Int.colorCompat
    get() = ResourcesCompat.getColor(BaseApplication.appContext.resources, this, null)

val Int.toDrawableCompat: Drawable?
    get() = BaseApplication.appContext.resources.getDrawable(this, null)

val Int.toStringCompat
    get() = BaseApplication.appContext.resources.getString(this)