package com.cyaan.core.ui.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import dagger.hilt.android.qualifiers.ApplicationContext

@ApplicationContext
private lateinit var context: Context

private val resource by lazy { context.resources }

val Int.colorCompat
    get() = ResourcesCompat.getColor(resource, this, null)

val Int.drawableCompat: Drawable?
    get() = ResourcesCompat.getDrawable(resource, this, null)

val Int.stringCompat
    get() = resource.getString(this)
