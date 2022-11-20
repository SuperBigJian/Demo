/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyaan.core.common.di

import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.cyaan.core.common.extension.MainHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun providePackageManager(@ApplicationContext context: Context): PackageManager =
        context.applicationContext.packageManager

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    fun provideMainThreadHandler() = MainHandler()

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            prettyPrint = true //json格式化
            isLenient = true //宽松解析，json格式异常也可解析，如：{name:"小红",age:"18"} + Person(val name:String,val age:Int) ->Person("小红",18)
            ignoreUnknownKeys = true //忽略未知键，如{"name":"小红","age":"18"} ->Person(val name:String)
            coerceInputValues = true //强制输入值，如果json属性与对象格式不符，则使用对象默认值，如：{"name":"小红","age":null} + Person(val name:String = "小绿"，val age:Int = 18) ->Person("小红",18)
            encodeDefaults = true //编码默认值,默认情况下，默认值的属性不会参与序列化，通过设置encodeDefaults = true,可让默认属性参与序列化(可参考上述例子)
            allowStructuredMapKeys = true //允许结构化映射(map的key可以使用对象)
            allowSpecialFloatingPointValues = true //特殊浮点值：允许Double为NaN或无穷大
        }
    }
}
