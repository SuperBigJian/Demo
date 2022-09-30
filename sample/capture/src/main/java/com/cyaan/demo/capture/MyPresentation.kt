package com.cyaan.demo.capture

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import android.view.SurfaceHolder
import com.cyaan.demo.capture.databinding.ActivitySecondBinding
import timber.log.Timber

class MyPresentation(context: Context, display: Display) : Presentation(context, display) {
    private val mBinding by lazy { ActivitySecondBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Timber.e("surfaceCreated $holder")
//                    ScreenCaptureManager.getProjection()?.createVirtualDisplay("screen_capture", 1280, 720, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, holder.surface, null, null)
                ScreenCaptureManager.setSurface(holder.surface)
                ScreenCaptureManager.toggleStream()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                Timber.d("surfaceChanged $holder")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })


    }
}