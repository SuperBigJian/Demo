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
            val outIndex = mMediaCodec.dequeueOutputBuffer(info, 10000)
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
        //跳过分隔符
        var offset = 4
        if (byteBuffer.get(2).toInt() == 0x01) {
            offset = 3
        }
//H264推流
        when (byteBuffer.get(offset) and 0x1F) {
            7 -> {
                vps_pps_sps = ByteArray(bufferInfo.size)
                vps_pps_sps?.let { byteBuffer.get(it) }
            }
            5 -> {
                val bytes = ByteArray(bufferInfo.size)
                byteBuffer.get(bytes)
                vps_pps_sps?.let {
                    val newBytes = ByteArray(it.size + bytes.size)
                    System.arraycopy(it, 0, newBytes, 0, it.size)
                    System.arraycopy(bytes, 0, newBytes, it.size, bytes.size)
                    sendData(newBytes)
                } ?: sendData(bytes)
            }
            else -> {
                val bytes = ByteArray(bufferInfo.size)
                byteBuffer.get(bytes)
                sendData(bytes)
            }
        }

//H265推流
//        when (byteBuffer.get(offset) and 0x7E shr 1) {
//            32 -> {
//                vps_pps_sps = ByteArray(bufferInfo.size)
//                vps_pps_sps?.let { byteBuffer.get(it) }
//            }
//            19 -> {
//                val bytes = ByteArray(bufferInfo.size)
//                byteBuffer.get(bytes)
//                vps_pps_sps?.let {
//                    val newBytes = ByteArray(it.size + bytes.size)
//                    System.arraycopy(it, 0, newBytes, 0, it.size)
//                    System.arraycopy(bytes, 0, newBytes, it.size, bytes.size)
//                    sendData(newBytes)
//                } ?: sendData(bytes)
//            }
//            else -> {
//                val bytes = ByteArray(bufferInfo.size)
//                byteBuffer.get(bytes)
//                sendData(bytes)
//            }
//        }
    }

    private fun sendData(bytes: ByteArray) {
        Timber.e("send data $bytes")
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