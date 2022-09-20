package com.cyaan.demo.capture

import android.graphics.Color
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
                it.data?.let { intent ->
                    ScreenCaptureManager.startCapture(this, intent)
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