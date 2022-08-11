package com.cyaan.module.stock.network

import kotlinx.serialization.Serializable


@Serializable
data class StockData(
    val dm: String,
    val mc: String,
    val jys: String
)
