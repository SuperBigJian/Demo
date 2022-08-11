package com.cyaan.module.stock.network

import retrofit2.http.GET

interface StockApiService {

    @GET("hslt/list")
    suspend fun fetchHSStockList(): List<StockData>
}