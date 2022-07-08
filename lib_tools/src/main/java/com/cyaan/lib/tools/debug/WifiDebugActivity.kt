package com.cyaan.lib.tools.debug

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ShellUtils
import com.cyaan.lib.business.router.RouterActivityPath
import com.cyaan.lib.common.ui.BaseActivity
import com.cyaan.lib.common.utils.OnClick
import com.cyaan.lib.tools.databinding.ToolsActivityWifiDebugBinding

@Route(path = RouterActivityPath.PAGER_WIFI_DEBUG)
class WifiDebugActivity : BaseActivity() {
    private val mBinding by lazy { ToolsActivityWifiDebugBinding.inflate(layoutInflater) }

    override fun initParam(intent: Intent?) {
        val str = ShellUtils.execCmd("su", false)
        val str1 = ShellUtils.execCmd("setprop service.adb.tcp.port 5555", true)
        val str2 = ShellUtils.execCmd("stop adbd", true)
        val str3 = ShellUtils.execCmd("start adbd", true)

        mBinding.result.text = "$str \n$str1 \n $str2 \n $str3"
    }

    override fun initView() {
        mBinding.back.OnClick { finish() }
        mBinding.ipTv.text = "ip: ${NetworkUtils.getIpAddressByWifi()}"
        setContentView(mBinding.root)
    }
}