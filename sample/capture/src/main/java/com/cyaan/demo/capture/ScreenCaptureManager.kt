package com.cyaan.demo.capture

import android.content.Context
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.ServiceUtils
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber

object ScreenCaptureManager {
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private val encoder = ScreenH264Encoder()
    private val decoder = ScreenH264Decoder()
    private val mScreenCaptureListenerList: MutableList<ScreenCaptureListener> = ArrayList()
    private val mMediaProjectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            mVirtualDisplay?.release()
            mVirtualDisplay = null
            mMediaProjection?.unregisterCallback(this)
            mMediaProjection = null
            encoder.releaseEncoder()
            decoder.releaseDecode()
            onScreenCaptureStopped()
            Timber.d("MediaProjection onStopped")
        }
    }

    fun startCapture(context: Context, projectionIntent: Intent) {
        Timber.e("startCapture")
        val mediaProjectionManager = context.getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(context, ScreenCaptureService::class.java)
            intent.putExtra(ScreenCaptureService.MEDIA_PROJECTION_INTENT, projectionIntent)
            context.startForegroundService(intent)
        } else {
            startScreenCapture(context, mediaProjectionManager.getMediaProjection(AppCompatActivity.RESULT_OK, projectionIntent))
        }
    }

    fun startScreenCapture(context: Context, mediaProjection: MediaProjection?) {
        if (mediaProjection != null) {
            mMediaProjection = mediaProjection
            encoder.setMediaProjection(context.resources.displayMetrics, mediaProjection)
            encoder.dataListener = {
                decoder.decodeData(it)
                FileIOUtils.writeFileFromBytesByStream("${context.filesDir}/encoder.h264", it, true)
            }
            mediaProjection.registerCallback(mMediaProjectionCallback, null)
            onScreenCaptureStarted()
        } else {
            Timber.e("MediaProjection is null")
        }
    }

    fun setSurface(surface: Surface?) {
        surface?.let {
            decoder.setSurface(surface)
        } ?: Timber.e("setSurface null")
    }

    fun stopCapture(context: Context) {
        Timber.e("stopCapture")
        encoder.stopEncoder()
        mMediaProjection?.stop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ServiceUtils.isServiceRunning(ScreenCaptureService::class.java)) {
            val intent = Intent(context, ScreenCaptureService::class.java)
            context.stopService(intent)
        }
    }

    fun toggleStream() {
        if (mMediaProjection == null) {
            Timber.e("please startCapture first")
            return
        }

        if (encoder.isEncoding()) {
            encoder.stopEncoder()
        } else {
            encoder.startEncoder()
        }
    }

    fun getProjection(): MediaProjection? {
        return mMediaProjection
    }

    fun registerListener(screenCaptureListener: ScreenCaptureListener) {
        if (!mScreenCaptureListenerList.contains(screenCaptureListener)) {
            mScreenCaptureListenerList.add(screenCaptureListener)
        }
    }

    fun unregisterListener(screenCaptureListener: ScreenCaptureListener) {
        if (mScreenCaptureListenerList.contains(screenCaptureListener)) {
            mScreenCaptureListenerList.remove(screenCaptureListener)
        }
    }

    private fun onScreenCaptureStarted() {
        for (screenCaptureListener in mScreenCaptureListenerList) {
            screenCaptureListener.onScreenCaptureStarted()
        }
    }

    private fun onScreenCaptureStopped() {
        for (screenCaptureListener in mScreenCaptureListenerList) {
            screenCaptureListener.onScreenCaptureStopped()
        }
    }

    interface ScreenCaptureListener {
        fun onScreenCaptureStarted()
        fun onScreenCaptureStopped()
    }
}