package com.cyaan.module.stock

import android.os.Bundle
import android.os.Debug
import android.view.View
import androidx.activity.viewModels
import com.cyaan.core.ui.app.BaseActivity
import com.cyaan.module.stock.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mViewModel: StockViewModel by viewModels()

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initView(): View {
        return mBinding.root
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