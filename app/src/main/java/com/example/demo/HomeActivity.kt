package com.example.demo

import android.content.Context
import com.cyaan.core.base.utils.OnClick
import com.cyaan.core.ui.app.BaseActivity
import com.example.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    @ApplicationContext
    private lateinit var mContext: Context

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(mBinding.root)
    }

    override fun setListener() {
        mBinding.wifi.text = mContext.resources.getString(R.string.app_name)


//        mBinding.wifi.text = R.string.app_name.stringCompat
        mBinding.back.OnClick { finish() }
        mBinding.wifi.OnClick {

        }
    }
}