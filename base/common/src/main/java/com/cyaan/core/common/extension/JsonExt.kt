package com.cyaan.core.common.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> Gson.fromJson(json: String?, success: T.() -> Unit = {}, error: () -> Unit = {}) = this.fromJson<T?>(json, genericType<T>())?.success() ?: error()

inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type

fun Any.toJson(): String = Gson().toJson(this)
