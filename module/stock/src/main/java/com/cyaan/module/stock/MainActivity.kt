package com.cyaan.module.stock

import android.os.Bundle
import android.os.Debug
import androidx.activity.viewModels
import com.cyaan.core.common.network.KResult
import com.cyaan.core.ui.app.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

//        mViewModel.stockState.observe(this) { result ->
//            if (result is KResult.Success) {
//                result.data.forEach {
//                    Timber.tag("chenjian").d("$it")
//                }
//            }
//            if (result is KResult.Error) {
//                Timber.e(result.exception)
//            }
//        }

        Debug.stopMethodTracing()
    }
}