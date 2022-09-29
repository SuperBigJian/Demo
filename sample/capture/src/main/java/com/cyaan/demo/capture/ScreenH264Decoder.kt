package com.cyaan.demo.capture

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.view.Surface
import timber.log.Timber

class ScreenH264Decoder {
    private val mMediaCodec by lazy { MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC) }

    private val width = 1280
    private val height = 720

    fun setSurface(surface: Surface) {
        MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 300)
            setInteger(MediaFormat.KEY_BIT_RATE, width * height)
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            mMediaCodec.configure(this, surface, null, 0)
        }
        mMediaCodec.start()
    }

    fun decodeData(data: ByteArray) {
        val index = mMediaCodec.dequeueInputBuffer(10000)
        if (index >= 0) {
            // 获取输入缓冲区
            val inputBuffer = mMediaCodec.getInputBuffer(index)
            inputBuffer?.clear()
            inputBuffer?.put(data, 0, data.size)
            mMediaCodec.queueInputBuffer(index, 0, data.size, System.currentTimeMillis(), 0)
        }
        val bufferInfo = MediaCodec.BufferInfo()
        var outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10000)
        while (outputBufferIndex > 0) {
            mMediaCodec.releaseOutputBuffer(outputBufferIndex, true)
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0)
        }
    }

    fun stopDecode() {
        mMediaCodec.stop()
    }

    fun releaseDecode() {
        mMediaCodec.stop()
        mMediaCodec.release()
    }
}