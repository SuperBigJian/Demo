package com.cyaan.demo.capture

import android.app.Presentation
import android.content.Context
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cyaan.demo.capture.databinding.ActivityMainBinding
import com.cyaan.demo.capture.databinding.ActivitySecondBinding
import timber.log.Timber
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mDisplayManager by lazy { this.getSystemService(DISPLAY_SERVICE) as DisplayManager }
    private var destroy = false
    private val captureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            it.data?.let { intent ->
                ScreenCaptureManager.startCapture(this, intent)
            }
        } else if (it.resultCode == RESULT_CANCELED) {
            Timber.d("screen recorder cancel")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                thread {
                    while (!destroy) {
                        val canvas = holder.lockCanvas()
                        val random = Random()
                        val r = random.nextInt(255)
                        val g = random.nextInt(255)
                        val b = random.nextInt(255)
                        canvas.drawColor(Color.rgb(r, g, b))
                        holder.unlockCanvasAndPost(canvas)
                        Thread.sleep(500)
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })

        val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        captureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())

        ScreenCaptureManager.registerListener(object : ScreenCaptureManager.ScreenCaptureListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScreenCaptureStarted() {
                Timber.d("onScreenCaptureStarted")
//                val surfaceView = SurfaceView(this@MainActivity)
//                ScreenCaptureManager.mRemote?.setSurface(surfaceView.holder.surface)

                val presentation = MyPresentation(this@MainActivity, mDisplayManager.displays[1])
                presentation.show()
            }

            override fun onScreenCaptureStopped() {
                Timber.d("onScreenCaptureStopped")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy = true
        ScreenCaptureManager.stopCapture(this)
    }

    class MyPresentation(context: Context, display: Display) : Presentation(context, display) {
        private val mBinding by lazy { ActivitySecondBinding.inflate(layoutInflater) }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(mBinding.root)
            ScreenCaptureManager.mRemote?.setSurface(mBinding.surfaceView.holder.surface)
        }
    }

}

