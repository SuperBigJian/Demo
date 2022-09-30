package com.cyaan.demo.capture

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.AppUtils
import timber.log.Timber

class ScreenCaptureService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground()
        val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        intent.getParcelableExtra<Intent>(MEDIA_PROJECTION_INTENT)?.let { project ->
            val mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, project)
            ScreenCaptureManager.startScreenCapture(this, mediaProjection)
        } ?: Timber.e("onStartCommand MEDIA_PROJECTION_INTENT = null")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    private fun startForeground() {
        val builder = NotificationBuilder(this)
        val notification = builder.buildNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    class NotificationBuilder(private val mContext: Context) {
        private val mNotificationManager: NotificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        fun buildNotification(): Notification {
            if (shouldCreateNowPlayingChannel()) {
                createNowPlayingChannel()
            }
            val builder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
            return builder.setContentTitle(mContext.getString(R.string.screen_capture_notification_title))
                .setContentText(AppUtils.getAppName() + mContext.getString(R.string.screen_capture_notification_text)).setSmallIcon(R.drawable.ic_screen_capture).build()
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun nowPlayingChannelExists(): Boolean {
            return mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null
        }

        private fun shouldCreateNowPlayingChannel(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !nowPlayingChannelExists()
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun createNowPlayingChannel() {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Capture",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Screen Recode Service"
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "screen-recode"
        const val NOTIFICATION_ID = 0xD660
        const val MEDIA_PROJECTION_INTENT = "MEDIA_PROJECTION_INTENT"
    }
}