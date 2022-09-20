package com.cyaan.demo.capture

import android.content.Intent
import android.graphics.Color
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cyaan.demo.capture.ScreenCaptureService.Companion.MEDIA_PROJECTION_INTENT
import com.cyaan.demo.capture.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var destroy = false
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { projection ->
                    ScreenCaptureProjection.getInstance().initContext(this)
                    ScreenCaptureProjection.getInstance().initDisplay(mBinding.display)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val intent = Intent(this, ScreenCaptureService::class.java)
                        intent.putExtra(MEDIA_PROJECTION_INTENT, projection)
                        startForegroundService(intent)
                    } else {
                        val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                        val mediaProjection = mediaProjectionManager.getMediaProjection(RESULT_OK, projection)
                        ScreenCaptureProjection.getInstance().startScreenCapture(mediaProjection)
                    }
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Timber.d("screen recorder cancel")
            }
        }.launch(mediaProjectionManager.createScreenCaptureIntent())

        mBinding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                thread {
                    while (!destroy) {
                        val canvans = holder.lockCanvas()
                        val random = Random()
                        val r = random.nextInt(255)
                        val g = random.nextInt(255)
                        val b = random.nextInt(255)
                        canvans.drawColor(Color.rgb(r, g, b))
                        holder.unlockCanvasAndPost(canvans)
                        Thread.sleep(1000)
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
                destroy = true
            }
        })
    }
}