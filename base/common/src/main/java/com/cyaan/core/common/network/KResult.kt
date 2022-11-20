package com.cyaan.core.common.network

sealed interface KResult<out T> {
    data class Success<T>(val data: T) : KResult<T>
    data class Error(val exception: Throwable? = null) : KResult<Nothing>
    object Loading : KResult<Nothing>
}