package com.cyaan.lib.common.result

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

sealed class KResult<out R> {
    data class Success<out T>(val data: T) : KResult<T>()
    data class Error(val exception: Exception) : KResult<Nothing>()
    object Loading : KResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val KResult<*>.succeeded
    get() = this is KResult.Success && data != null

fun <T> KResult<T>.successOr(fallback: T): T {
    return (this as? KResult.Success<T>)?.data ?: fallback
}

val <T> KResult<T>.data: T?
    get() = (this as? KResult.Success)?.data

inline fun <reified T> KResult<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is KResult.Success) {
        liveData.value = data
    }
}

inline fun <reified T> KResult<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is KResult.Success) {
        stateFlow.value = data
    }
}
