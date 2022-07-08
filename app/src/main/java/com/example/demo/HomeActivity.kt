package com.example.demo

import com.cyaan.lib.business.router.RouterActivityPath
import com.cyaan.lib.business.router.routeTo
import com.cyaan.lib.common.ui.BaseActivity
import com.cyaan.lib.common.utils.OnClick
import com.example.demo.databinding.ActivityMainBinding

class HomeActivity : BaseActivity() {

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(mBinding.root)
    }

    override fun setListener() {
        mBinding.back.OnClick { finish() }
        mBinding.wifi.OnClick {
            routeTo(RouterActivityPath.PAGER_WIFI_DEBUG)
        }
    }
}