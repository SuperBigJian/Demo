package com.cyaan.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyaan.demo.compose.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                myApp()
            }
        }
    }
}

@Composable
fun myApp() {
    var showWelcome by rememberSaveable { mutableStateOf(true) }
    if (showWelcome) {
        WelcomeContent {
            showWelcome = false
        }
    } else {
        Greetings()
    }
}

@Composable
fun WelcomeContent(onWelClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to compose!")
        Button(modifier = Modifier.padding(top = 24.dp), onClick = onWelClick) {
            Text(text = "choose")
        }
    }
}


@Composable
fun Greetings(
    text: List<String> = List(20) {
        "No.$it"
    }
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = text) { num ->
            Greeting(name = num)
        }
    }
}

@Composable
fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(if (expanded) 48.dp else 0.dp)

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = extraPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)

            ) {
                Text(text = "Hello,")
                Text(text = "$name")
            }
            OutlinedButton(
                onClick = {
                    expanded = !expanded
                }, modifier = Modifier
                    .height(60.dp)
                    .width(120.dp)
            ) {
                Text(text = if (expanded) "show less" else "show more")
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun MyAppPreview() {
    DemoTheme {
        myApp()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DemoTheme {
        Greeting("android")
    }
}
