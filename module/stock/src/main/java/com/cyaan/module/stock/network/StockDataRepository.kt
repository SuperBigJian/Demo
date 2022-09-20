package com.cyaan.module.stock.network

import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class StockDataRepository @Inject constructor(private var network: StockNetworkDataSource) {

    fun fetchHSStockList() = flow {
        emit(network.fetchHSStockList())
    }
}