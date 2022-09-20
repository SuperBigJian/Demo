package com.cyaan.module.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cyaan.core.common.network.KResult
import com.cyaan.core.common.network.asResult
import com.cyaan.core.ui.app.BaseViewModel
import com.cyaan.module.stock.network.StockData
import com.cyaan.module.stock.network.StockDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val stockDataRepository: StockDataRepository) : BaseViewModel() {

    private val stockListFlow: Flow<KResult<List<StockData>>> = stockDataRepository.fetchHSStockList().asResult()


    val stockState: LiveData<KResult<List<StockData>>> = stockListFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = KResult.Success(emptyList())
    ).asLiveData()

}