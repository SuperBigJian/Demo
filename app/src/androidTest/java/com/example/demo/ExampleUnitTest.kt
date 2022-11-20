package com.example.demo

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cyaan.core.common.utils.onClick
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {

    @Test
    fun testHomeActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), HomeActivity::class.java)
        val scenario = ActivityScenario.launch<HomeActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)
        val countdown = CountDownLatch(1)
        scenario.onActivity {
            it.mBinding.wifi.onClick {
                countdown.countDown()
            }
        }
        countdown.await()
    }
}