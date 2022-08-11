package com.cyaan.module.stock.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject


class StockDataRepository @Inject constructor(private var network: StockNetworkDataSource) {

    fun fetchHSStockList() = flow {
        emit(network.fetchHSStockList())
    }
}