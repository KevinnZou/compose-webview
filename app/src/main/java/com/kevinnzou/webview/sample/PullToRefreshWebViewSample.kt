package com.kevinnzou.webview.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    private val initialUrl = "https://www.thelinehotel.com/wp-admin" //"https://github.com/KevinnZou/compose-webview"

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
                var contentHeight by remember { mutableIntStateOf(0) }
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
                    LazyColumn {
                        item {
                            // A custom WebViewClient and WebChromeClient can be provided via subclassing
                            val webClient = remember {
                                object : AccompanistWebViewClient() {
                                    override fun onPageStarted(
                                        view: WebView,
                                        url: String?,
                                        favicon: Bitmap?
                                    ) {
                                        super.onPageStarted(view, url, favicon)
                                        Log.d(
                                            "Accompanist WebView",
                                            "Page started loading for $url"
                                        )
                                    }
                                    override fun onPageFinished(view: WebView, url: String?) {
                                        super.onPageFinished(view, url)
                                        contentHeight = view.contentHeight // Get the content height of the WebView
                                        Log.d(
                                            "onPageFinished",
                                            "content Height $contentHeight"
                                        )
                                    }
                                }
                            }

                            WebView(
                                state = state,
                                modifier = if (contentHeight < 1) {
                                    Modifier.fillParentMaxSize()
                                } else {
                                    Modifier
                                        .fillMaxSize()
                                },
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
}