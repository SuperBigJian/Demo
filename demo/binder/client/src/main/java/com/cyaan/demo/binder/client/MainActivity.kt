package com.cyaan.demo.binder.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyaan.core.ui.compose.theme.AppTheme
import com.cyaan.demo.binder.ITestAidlInterface
import com.cyaan.demo.binder.remote.UserInfo
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private var mRemote: ITestAidlInterface? = null
    private var content by mutableStateOf("Binder Test")
    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.d("onServiceConnected $name")
            content = "remote connect"
            mRemote = ITestAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mRemote = null
            content = "remote disconnect"
            Timber.d("onServiceDisconnected $name")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting({
                        connectRemoteService()
                    }, {
                        disconnectRemoteService()
                    }, {
                        val info = UserInfo()
                        Timber.d(info.toString())
                        mRemote?.getInfoById(2, info)
                        Timber.d(info.toString())
                        Log.d("chenjian", "onCreate: $info")
                    })
                }
            }
        }
    }

    private fun connectRemoteService() {
        val intent = Intent().apply {
            setAction("com.cyaan.demo.binder.remote.UserManager")
            setPackage("com.cyaan.demo.binder.remote")
        }

        bindService(intent, serviceConn, BIND_AUTO_CREATE)
    }

    private fun disconnectRemoteService() {
        try {
            unbindService(serviceConn)
        } catch (e: Exception) {

        }
    }

    @Composable
    fun Greeting(connect: () -> Unit = {}, disconnect: () -> Unit = {}, test: () -> Unit = {}) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(top = 50.dp), text = content, fontSize = 32.sp)
            Buttons(modifier = Modifier.fillMaxSize(), connect, disconnect, test)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AppTheme {
            Greeting({}, {}, {})
        }
    }
}


@Composable
fun Buttons(modifier: Modifier = Modifier, connect: () -> Unit = {}, disconnect: () -> Unit = {}, test: () -> Unit = {}) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = connect) {
            Text(modifier = Modifier.padding(20.dp), text = "连接")
        }

        Button(modifier = Modifier.padding(20.dp), onClick = disconnect) {
            Text(modifier = Modifier.padding(20.dp), text = "断开")
        }

        Button(modifier = Modifier.padding(20.dp), onClick = test) {
            Text(modifier = Modifier.padding(20.dp), text = "测试")
        }
    }
}

