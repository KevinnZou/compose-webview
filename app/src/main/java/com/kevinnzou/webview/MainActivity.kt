package com.kevinnzou.webview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kevinnzou.webview.sample.WebViewSaveStateSample
import com.kevinnzou.webview.sample.BasicWebViewSample
import com.kevinnzou.webview.sample.PullToRefreshWebViewSample
import com.kevinnzou.webview.ui.theme.ComposewebviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewebviewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        BasicWebViewSample::class.java
                                    )
                                )
                            }) {
                                Text("Basic")
                            }
                            Button(onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        WebViewSaveStateSample::class.java
                                    )
                                )
                            }) {
                                Text("Save State")
                            }
                            Button(onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        PullToRefreshWebViewSample::class.java
                                    )
                                )
                            }) {
                                Text("Pull to Refresh")
                            }
                        }
                    }
                }
            }
        }
    }
}