package com.kevinnzou.webview.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.fengdai.compose.pulltorefresh.PullToRefresh
import com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState
import com.kevinnzou.web.AccompanistWebViewClient
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.kevinnzou.webview.ui.theme.ComposewebviewTheme
import kotlinx.coroutines.delay

class PullToRefreshWebViewSample : ComponentActivity() {
    val initialUrl = "https://github.com/KevinnZou/compose-webview"

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewebviewTheme {
                val state = rememberWebViewState(url = initialUrl)
                val navigator = rememberWebViewNavigator()
                var textFieldValue by remember(state.lastLoadedUrl) {
                    mutableStateOf(state.lastLoadedUrl)
                }

                var refreshing by remember { mutableStateOf(false) }
                LaunchedEffect(refreshing) {
                    if (refreshing) {
                        navigator.reload()
                        delay(1200)
                        refreshing = false
                    }
                }

                PullToRefresh(
                    state = rememberPullToRefreshState(isRefreshing = refreshing),
                    onRefresh = { refreshing = true }
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(state = rememberScrollState())
                    ) {
                        // A custom WebViewClient and WebChromeClient can be provided via subclassing
                        val webClient = remember {
                            object : AccompanistWebViewClient() {
                                override fun onPageStarted(
                                    view: WebView,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    super.onPageStarted(view, url, favicon)
                                    Log.d("Accompanist WebView", "Page started loading for $url")
                                }
                            }
                        }

                        WebView(
                            state = state,
                            modifier = Modifier
                                .fillMaxSize(),
                            navigator = navigator,
                            onCreated = { webView ->
                                webView.settings.javaScriptEnabled = true
                            },
                            client = webClient
                        )
                    }
                }
            }
        }
    }
}