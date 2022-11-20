package com.cyaan.core.common.network

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    var code: Int,
    var msg: String,
    var data: T? = null
) {
    companion object {
        const val RESPONSE_SUCCESS = 0
    }
}