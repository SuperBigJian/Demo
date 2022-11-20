package com.cyaan.core.ui.extension


import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cyaan.core.ui.R

fun ImageView.loadImageWithUrl(imageUrl: String) {
    if (!isValidContextForGlide(this.context)) return
    val options = RequestOptions()
        .placeholder(R.color.white)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .dontAnimate()
        .error(R.color.white)
    Glide.with(this)
        .load(imageUrl)
        .apply(options)
        .into(this)
}

fun View.loadBackgroundWithUrl(imageUrl: String) {
    if (!isValidContextForGlide(this.context)) return
    val options = RequestOptions()
        .placeholder(R.color.white)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .dontAnimate()
        .error(R.color.white)

    Glide.with(this)
        .load(imageUrl)
        .apply(options)
        .into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                this@loadBackgroundWithUrl.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })
}

private fun isValidContextForGlide(context: Context?): Boolean {
    return context?.let {
        if (it is Activity) {
            if (it.isDestroyed || it.isFinishing) {
                return false
            }
        }
        true
    } ?: false
}