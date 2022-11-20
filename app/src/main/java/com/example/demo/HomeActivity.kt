package com.example.demo

import com.cyaan.core.common.extension.onClick
import com.cyaan.core.ui.app.BaseActivity
import com.example.demo.databinding.ActivityMainBinding

class HomeActivity : BaseActivity() {

    val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(mBinding.root)
    }

    override fun setListener() {
//        mBinding.wifi.text = R.string.app_name.stringCompat
        mBinding.back.onClick { finish() }
        mBinding.wifi.onClick {

        }
    }
}