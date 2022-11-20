package com.cyaan.module.stock.di

import com.cyaan.core.common.network.NetworkConnectionInterceptor
import com.cyaan.core.common.network.OkHttpExceptionInterceptor
import com.cyaan.core.common.network.logInterceptor
import com.cyaan.module.stock.network.StockApiService
import com.cyaan.module.stock.network.StockParamsInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StockNetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(jsonParse: Json, okHttpClient: OkHttpClient): StockApiService {
        return Retrofit.Builder()
            .baseUrl("http://api.mairui.club")
            .client(
                okHttpClient.newBuilder()
                    .addInterceptor(StockParamsInterceptor)
                    .addInterceptor(logInterceptor())
                    .build()
            )
            .addConverterFactory(jsonParse.asConverterFactory("application/json".toMediaType()))
            .build().create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            addInterceptor(OkHttpExceptionInterceptor)
            addInterceptor(NetworkConnectionInterceptor)
        }.build()
    }

    companion object {
        const val WRITE_TIMEOUT = 10000L
        const val READ_TIMEOUT = 10000L
        const val CONNECT_TIMEOUT = 10000L
    }
}