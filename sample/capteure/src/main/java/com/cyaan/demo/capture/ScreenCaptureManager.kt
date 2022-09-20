package com.cyaan.demo.capture

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity

object ScreenCaptureManager {

    fun startCapture(activity: AppCompatActivity, projectionIntent: Intent) {
        val mediaProjectionManager = activity.getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        ScreenCaptureProjection.getInstance().initContext(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(activity, ScreenCaptureService::class.java)
            intent.putExtra(ScreenCaptureService.MEDIA_PROJECTION_INTENT, projectionIntent)
            activity.startForegroundService(intent)
        } else {
            val mediaProjection = mediaProjectionManager.getMediaProjection(AppCompatActivity.RESULT_OK, projectionIntent)
            ScreenCaptureProjection.getInstance().startScreenCapture(mediaProjection)
        }
    }

    fun setSurface(surface: Surface){
        ScreenCaptureProjection.getInstance().initDisplay(surface)
    }


    fun stopCapture() {

    }

}