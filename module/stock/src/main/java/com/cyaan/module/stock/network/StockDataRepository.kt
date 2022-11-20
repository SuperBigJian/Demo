package com.cyaan.module.stock.network

import com.cyaan.core.common.network.KResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class StockDataRepository @Inject constructor(private var network: StockNetworkDataSource) {

    private val getListParamsFlow = MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST).apply {
        tryEmit(1)
    }

    val stockListStream = stockListStream()

    fun fetchHSStockList() {
        getListParamsFlow.tryEmit(1)
    }

    private fun stockListStream(): Flow<KResult<List<StockData>>> {
        return getListParamsFlow.map {
            network.fetchHSStockList()
        }.map {
            if (it.isNotEmpty()) {
                KResult.Success(it)
            } else {
                KResult.Error(NullPointerException("response data is empty"))
            }
        }.onStart { emit(KResult.Loading) }
            .catch { emit(KResult.Error(it)) }
    }
}