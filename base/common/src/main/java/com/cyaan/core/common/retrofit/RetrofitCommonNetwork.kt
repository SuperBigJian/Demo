package com.cyaan.core.common.retrofit

import com.cyaan.core.common.di.DefaultOkHttpClient
import com.cyaan.core.common.di.NoTokenOkHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RetrofitCommonNetwork {

    @Provides
    fun provideRetrofit(@DefaultOkHttpClient client: OkHttpClient, jsonParse: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(jsonParse.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideNoTokenRetrofit(@NoTokenOkHttpClient client: OkHttpClient, jsonParse: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(jsonParse.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}