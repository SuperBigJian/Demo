package com.example.demo

import com.cyaan.core.common.utils.OnClick
import com.cyaan.core.ui.app.BaseActivity
import com.cyaan.core.ui.extension.stringCompat
import com.example.demo.databinding.ActivityMainBinding

class HomeActivity : BaseActivity() {

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(mBinding.root)
    }

    override fun setListener() {
//        mBinding.wifi.text = R.string.app_name.stringCompat
        mBinding.back.OnClick { finish() }
        mBinding.wifi.OnClick {

        }
    }
}