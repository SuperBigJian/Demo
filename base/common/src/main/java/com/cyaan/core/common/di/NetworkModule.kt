package com.cyaan.core.common.di

import com.cyaan.core.common.retrofit.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val WRITE_TIMEOUT = 10000L
    private const val READ_TIMEOUT = 10000L
    private const val CONNECT_TIMEOUT = 10000L

    @Provides
    @Singleton
    fun provideJsonParse(): Json = Json {
        prettyPrint = true //json格式化
        isLenient = true //宽松解析，json格式异常也可解析，如：{name:"小红",age:"18"} + Person(val name:String,val age:Int) ->Person("小红",18)
        ignoreUnknownKeys = true //忽略未知键，如{"name":"小红","age":"18"} ->Person(val name:String)
        coerceInputValues = true //强制输入值，如果json属性与对象格式不符，则使用对象默认值，如：{"name":"小红","age":null} + Person(val name:String = "小绿"，val age:Int = 18) ->Person("小红",18)
        encodeDefaults = true //编码默认值,默认情况下，默认值的属性不会参与序列化，通过设置encodeDefaults = true,可让默认属性参与序列化(可参考上述例子)
        explicitNulls = true //序列化时是否忽略null
        allowStructuredMapKeys = true //允许结构化映射(map的key可以使用对象)
        allowSpecialFloatingPointValues = true //特殊浮点值：允许Double为NaN或无穷大
    }

    @DefaultOkHttpClient
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            addInterceptor(logInterceptor())
            addInterceptor(RequestInterceptor())
            addInterceptor(CommonParamsInterceptor)
            addInterceptor(ResponseInterceptor)
            addInterceptor(OkHttpExceptionInterceptor)
            addInterceptor(NetworkConnectionInterceptor)
        }.build()
    }

    @NoTokenOkHttpClient
    @Provides
    @Singleton
    fun provideNoTokenOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            addInterceptor(logInterceptor())
            addInterceptor(RequestInterceptor(false))
            addInterceptor(CommonParamsInterceptor)
            addInterceptor(ResponseInterceptor)
            addInterceptor(OkHttpExceptionInterceptor)
            addInterceptor(NetworkConnectionInterceptor)
        }.build()
    }
}