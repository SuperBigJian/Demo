package com.cyaan.module.stock.network

import javax.inject.Inject

class StockNetworkDataSource @Inject constructor(private val api: StockApiService) {

    suspend fun fetchHSStockList(): List<StockData> {
        return api.fetchHSStockList()
    }
}