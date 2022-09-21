package com.cyaan.demo.capture

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ServiceUtils
import timber.log.Timber

object ScreenCaptureManager {
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null

    private val mScreenCaptureListenerList: MutableList<ScreenCaptureListener> = ArrayList()
    private val mMediaProjectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            mVirtualDisplay?.release()
            mVirtualDisplay = null
            mMediaProjection?.unregisterCallback(this)
            mMediaProjection = null
            onScreenCaptureStopped()
            Timber.d("MediaProjection onStopped")
        }
    }

    var mRemote: IMyAidlInterface? = null

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.d("onServiceConnected $name")
            mRemote = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mRemote = null
            Timber.d("onServiceDisconnected $name")
        }
    }

    fun startCapture(context: Context, projectionIntent: Intent) {
        Timber.e("startCapture")
        val mediaProjectionManager = context.getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(context, ScreenCaptureService::class.java)
            intent.putExtra(ScreenCaptureService.MEDIA_PROJECTION_INTENT, projectionIntent)
            context.bindService(intent, serviceConn, ComponentActivity.BIND_AUTO_CREATE)
        } else {
            startScreenCapture(context, mediaProjectionManager.getMediaProjection(AppCompatActivity.RESULT_OK, projectionIntent))
        }
    }

    fun startScreenCapture(context: Context, mediaProjection: MediaProjection?) {
        if (mediaProjection != null) {
            mMediaProjection = mediaProjection
            createVirtualDisplay(context)
        } else {
            Timber.e("MediaProjection is null")
        }
    }

    private fun createVirtualDisplay(context: Context) {
        val metrics = context.resources.displayMetrics
        mVirtualDisplay = mMediaProjection?.createVirtualDisplay(
            this::class.java.simpleName,
            metrics.widthPixels,
            metrics.heightPixels,
            metrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            null,
            null,
            null
        )
        mMediaProjection?.registerCallback(mMediaProjectionCallback, null)
        onScreenCaptureStarted()
    }

    fun setSurface(surface: Surface?) {
        mVirtualDisplay?.let {
            it.surface = surface
        } ?: Timber.e("please startCapture first")
    }

    fun getSurface(): Surface? {
        return mVirtualDisplay?.surface
    }

    fun stopCapture(context: Context) {
        Timber.e("stopCapture")
        mMediaProjection?.stop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ServiceUtils.isServiceRunning(ScreenCaptureService::class.java)) {
            val intent = Intent(context, ScreenCaptureService::class.java)
            context.stopService(intent)
        }
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