package com.example.demo

import com.cyaan.core.base.utils.OnClick
import com.cyaan.core.ui.app.BaseActivity
import com.example.demo.databinding.ActivityMainBinding

class HomeActivity : BaseActivity() {

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(mBinding.root)
    }

    override fun setListener() {
        mBinding.back.OnClick { finish() }
        mBinding.wifi.OnClick {

        }
    }
}