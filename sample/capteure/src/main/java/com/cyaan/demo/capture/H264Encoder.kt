package com.cyaan.demo.capture

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodec.CONFIGURE_FLAG_ENCODE
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.view.Surface

class H264Encoder : Thread() {

    private var encoder: MediaCodec? = null

    private var msurface: Surface? = null
    private var mmediaProjection: MediaProjection? = null
    private val width = 720
    private val height = 1080


    override fun run() {
        encoder?.start()
        val info = MediaCodec.BufferInfo()
        while (true) {
            val outIndex = encoder?.dequeueOutputBuffer(info, 10000)?:0
            if (outIndex > 0) {
                //TODO 输出帧数据
                encoder?.releaseOutputBuffer(outIndex, true)
            }
        }
    }

    fun setMediaProjection(projection: MediaProjection) {
        mmediaProjection = projection
    }

    fun setRenderSurface(surface: Surface) {
        encoder =  MediaCodec.createEncoderByType("video/avc")
        msurface = surface
        val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 300)
            setInteger(MediaFormat.KEY_BIT_RATE, width * height)
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        }
        encoder?.configure(format, surface, null, CONFIGURE_FLAG_ENCODE)
        mmediaProjection?.createVirtualDisplay("screen_capture", width, height, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, encoder?.createInputSurface(), null, null)
    }
}