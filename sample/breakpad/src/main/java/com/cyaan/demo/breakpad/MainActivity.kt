package com.cyaan.demo.breakpad

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cyaan.core.ui.compose.theme.AppTheme
import com.cyaan.lib.breakpad.BreakpadDumper
import java.io.File


class MainActivity : ComponentActivity() {
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100


    private var externalReportPath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                CrashHome()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            );
        } else {
            initExternalReportPath();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initExternalReportPath();
    }

    private fun initExternalReportPath() {
        externalReportPath = File(Environment.getExternalStorageDirectory(), "crashDump")
        if (externalReportPath?.exists() != true) {
            externalReportPath?.mkdirs()
        }
    }

    /**
     * 一般来说，crash捕获初始化都会放到Application中，这里主要是为了大家有机会可以把崩溃文件输出到sdcard中
     * 做进一步的分析
     */
    private fun initBreakPad() {
        if (externalReportPath == null) {
            externalReportPath = File(filesDir, "crashDump")
            if (!externalReportPath!!.exists()) {
                externalReportPath!!.mkdirs()
            }
        }
        BreakpadDumper.initBreakpad(externalReportPath!!.absolutePath)
    }

    @Composable
    fun CrashHome() {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "click button create native crash")
            Button(onClick = {
                initBreakPad()
                CrashLib.crashDump()
            }) {
                Text(text = "crash")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun HomePreview() {
        AppTheme {
            CrashHome()
        }
    }
}