package com.cyaan.module.stock.di

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
import timber.log.Timber
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
                    .build()
            )
            .addConverterFactory(jsonParse.asConverterFactory("application/json".toMediaType()))
            .build().create()
    }
}