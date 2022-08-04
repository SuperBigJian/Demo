package com.cyaan.core.common.usecase

import com.cyaan.core.common.result.KResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): KResult<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    KResult.Success(it)
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            KResult.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}