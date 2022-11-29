package com.cyaan.demo.capture

import android.Manifest
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.view.SurfaceHolder
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.cyaan.core.common.extension.onClick
import com.cyaan.core.ui.app.BaseActivity
import com.cyaan.demo.capture.databinding.ActivityMainBinding
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import java.util.*
import kotlin.concurrent.thread

@RuntimePermissions
class MainActivity : BaseActivity() {
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

    override fun initView(): View {
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
                Timber.d("surfaceChanged $holder")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
        return mBinding.root
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun setListener() {
        mBinding.start.onClick {
            val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            captureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
        }

        mBinding.toggle.onClick {
            ScreenCaptureManager.toggleStream()
        }

        ScreenCaptureManager.registerListener(object : ScreenCaptureManager.ScreenCaptureListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScreenCaptureStarted() {
                Timber.d("onScreenCaptureStarted")
//                ScreenCaptureManager.setSurface(mBinding.surfaceView2.holder.surface)
//                ScreenCaptureManager.toggleStream()

//                Intent(this@MainActivity, SecondActivity::class.java).apply {
//                    val options = ActivityOptions.makeBasic()
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    options.launchDisplayId = displays.first().displayId
//                    startActivity(this, options.toBundle())
//                }
                val displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
                val presentation = MyPresentation(this@MainActivity, displays.first())
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
}

