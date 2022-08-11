package com.cyaan.module.stock.network

import okhttp3.Interceptor
import okhttp3.Response

object StockParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()
        val newUrl = currentRequest.url
            .newBuilder()
            .addPathSegment("57ba79032f0b2c2dfe")
            .build()

        return chain.proceed(currentRequest.newBuilder().url(newUrl).build())
    }
}