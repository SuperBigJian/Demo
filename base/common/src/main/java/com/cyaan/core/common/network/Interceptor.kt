package com.cyaan.core.common.network

import com.blankj.utilcode.util.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.IOException

/**
 * 线上bug：
 * Fatal Exception: java.lang.SecurityException: Permission denied (missing INTERNET permission?)
 * android.system.GaiException: android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)
 */
object OkHttpExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Throwable) {
            if (e is IOException) {
                throw e
            } else {
                throw IOException(e)
            }
        }
    }
}

object NetworkConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!NetworkUtils.isAvailable()) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = "network error!"
    }
}

fun logInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor { message -> Timber.i("HTTP: $message") }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}