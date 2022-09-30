package com.cyaan.demo.capture

import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface

class ScreenH264Decoder {
    private val mMediaCodec by lazy { MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC) }

    private val width = 1920
    private val height = 720

    fun setSurface(surface: Surface) {
        MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, 20)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
            setInteger(MediaFormat.KEY_BIT_RATE, width * height)
            mMediaCodec.configure(this, surface, null, 0)
        }
        mMediaCodec.start()
    }

    fun decodeData(bytes: ByteArray) {
        val index = mMediaCodec.dequeueInputBuffer(10000)
        if (index >= 0) {
            // 获取输入缓冲区
            mMediaCodec.getInputBuffer(index)?.apply {
                clear()
                put(bytes, 0, bytes.size)
            }

            mMediaCodec.queueInputBuffer(index, 0, bytes.size, System.currentTimeMillis(), 0)
        }
        val bufferInfo = MediaCodec.BufferInfo()
        var outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10000)
        while (outputBufferIndex > 0) {
            mMediaCodec.releaseOutputBuffer(outputBufferIndex, true)
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0)
        }
    }

    fun releaseDecode() {
        mMediaCodec.stop()
        mMediaCodec.release()
    }
}