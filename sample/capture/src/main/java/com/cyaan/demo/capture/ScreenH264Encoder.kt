package com.cyaan.demo.capture

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodec.CONFIGURE_FLAG_ENCODE
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.util.DisplayMetrics
import okhttp3.internal.and
import timber.log.Timber
import java.nio.ByteBuffer

class ScreenH264Encoder : Thread() {
    // I帧
    private val TYPE_FRAME_INTERVAL = 19

    // vps帧
    private val TYPE_FRAME_VPS = 32

    // 记录vps pps sps
    private var vps_pps_sps: ByteArray? = null

    private val mMediaCodec: MediaCodec by lazy {
        MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
    }

    var dataListener: (bytes: ByteArray) -> Unit = {}

    private var running = false

    fun setMediaProjection(display: DisplayMetrics, projection: MediaProjection) {
        val width = display.widthPixels
        val height = display.heightPixels
        val density = display.densityDpi
        MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 30)
            setInteger(MediaFormat.KEY_BIT_RATE, width * height)
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            mMediaCodec.configure(this, null, null, CONFIGURE_FLAG_ENCODE)
        }
        projection.createVirtualDisplay("screen_capture", width, height, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mMediaCodec.createInputSurface(), null, null)
    }

    override fun run() {
        mMediaCodec.start()
        running = true
        val info = MediaCodec.BufferInfo()
        while (true) {
            val outIndex = mMediaCodec.dequeueOutputBuffer(info, 10000) ?: 0
            if (outIndex > 0) {
                // 获取编码之后的数据输出流队列
                mMediaCodec.getOutputBuffer(outIndex)?.let {
                    encodeData(it, info)
                }
                mMediaCodec.releaseOutputBuffer(outIndex, false)
            }
        }
    }

    private fun encodeData(byteBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        // 偏移4 00 00 00 01为分隔符需要跳过
        var offSet = 4
        if (byteBuffer[2].toInt() == 0x01) {
            offSet = 3
        }
        // 判断当前帧类型
        when (byteBuffer[offSet] and 0x7E shr 1) {
            TYPE_FRAME_VPS -> {
                vps_pps_sps = ByteArray(bufferInfo.size).also {
                    byteBuffer.get(it)
                }
            }
            TYPE_FRAME_INTERVAL -> {
                // 将保存的vps sps pps添加到I帧前
                vps_pps_sps?.let {
                    val bytes = ByteArray(it.size + bufferInfo.size)
                    System.arraycopy(it, 0, bytes, 0, it.size)
                    byteBuffer.get(bytes, it.size, bytes.size)
                    sendData(bytes)
                } ?: run {
                    val bytes = ByteArray(bufferInfo.size)
                    byteBuffer.get(bytes)
                    sendData(bytes)
                }
            }
            else -> {
                // B帧 P帧 直接发送
                val bytes = ByteArray(bufferInfo.size)
                byteBuffer[bytes]
                sendData(bytes)
            }
        }
    }

    private fun sendData(bytes: ByteArray) {
        Timber.e("send data ${bytes.size}")
        dataListener.invoke(bytes)
    }

    private fun stopEncoder() {
        mMediaCodec.stop()
        running = false
    }

    private fun releaseEncoder() {
        stopEncoder()
        mMediaCodec.release()
    }

}