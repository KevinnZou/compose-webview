/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DEPRECATION")

package com.kevinnzou.webview.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberSaveableWebViewState
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState

class WebViewSaveStateSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    Column(Modifier.fillMaxSize()) {
                        Row {
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Home")
                            }
                            Button(onClick = { navController.navigate("detail") }) {
                                Text("Detail")
                            }
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("home") {
                                Home()
                            }
                            composable("detail") {
                                Detail()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Home() {
    val webViewState = rememberSaveableWebViewState()
    val navigator = rememberWebViewNavigator()

    LaunchedEffect(navigator) {
        val bundle = webViewState.viewState
        if (bundle == null) {
            // This is the first time load, so load the home page.
            navigator.loadUrl("https://developer.android.com/")
        }
    }

    WebView(
        state = webViewState,
        navigator = navigator,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun Detail() {
    val webViewState = rememberWebViewState(url = "https://developer.android.com/jetpack/compose")

    WebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize()
    )
}
