package com.cyaan.core.common.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

sealed interface KResult<out T> {
    data class Success<T>(val data: T) : KResult<T>
    data class Error(val exception: Throwable? = null) : KResult<Nothing>
    object Loading : KResult<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<KResult<T>> {
    return this
        .map<T, KResult<T>> {
            KResult.Success(it)
        }.onStart { emit(KResult.Loading) }
        .catch { emit(KResult.Error(it)) }
}