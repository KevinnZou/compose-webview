package com.kevinnzou.webview.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kevinnzou.webview.ui.theme.ComposewebviewTheme

class PullToRefreshWebViewSample : ComponentActivity() {
//    val initialUrl = "https://stackoverflow.com/questions/69199334/trying-to-add-a-refresh-method-to-a-webview-and-having-issues"
    val initialUrl = "https://www.thelinehotel.com/wp-admin"


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewebviewTheme {

                /* val state = rememberWebViewState(url = initialUrl)
                 val navigator = rememberWebViewNavigator()
                 var textFieldValue by remember(state.lastLoadedUrl) {
                     mutableStateOf(state.lastLoadedUrl)
                 }
                 val refreshScope = rememberCoroutineScope()
                 var refreshing by remember { mutableStateOf(false) }

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

                 fun refresh() = refreshScope.launch {
                     refreshing = true
                     delay(1500)
                     refreshing = false
                 }

                 val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

                 LaunchedEffect(key1 = refreshing) {
                     Log.d("saqiii", "onCreate: $refreshing")
                     if (refreshing) {
                         navigator.reload()
                         delay(1000)
                         refreshing = false
                     }
                 }

                 Box(
                     Modifier
                         .fillMaxSize()
                         .pullRefresh(pullRefreshState),
                     contentAlignment = Alignment.Center
                 ) {
                     WebView(
                         state = state,
                         modifier = Modifier
                             .background(Color.White)
                             .fillMaxSize(),
                         navigator = navigator,
                         onCreated = { webView ->
                             webView.settings.javaScriptEnabled = true
                         },
                         client = webClient
                     )
                     PullRefreshIndicator(
                         refreshing, pullRefreshState,
                         Modifier
                             .wrapContentSize()
                             .align(Alignment.TopCenter)
                     )
                 }*/
                WebViewPage(initialUrl)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WebViewPage2(url: String) {
    val context = LocalContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    //WEB-VIEW
    var contentHeight by remember { mutableIntStateOf(0) }
    val webView = remember(context) {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            setBackgroundColor(0x000000)
            webChromeClient = object : WebChromeClient()
            {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    LinearProgressIndicatorProgress = newProgress / 100f
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
                    isRefreshing = false
                    view?.evaluateJavascript(
                        "(function() { return document.body.scrollHeight; })();"
                    ) { height ->
                        contentHeight = height?.toInt() ?: 0
                        Log.d("saqiii", "onPageFinished:$contentHeight")
                    }
                }
            }
            loadUrl(url)
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            webView.reload()
        }
    )

    //PROGRESS INDICATOR FOR WEB-VIEW
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CompositionLocalProvider(LocalContext provides context) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (LinearProgressIndicatorProgress < 1f) {
                        LinearProgressIndicator(
                            progress = LinearProgressIndicatorProgress,
                            color = Color(0xffae52de),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AndroidView(
                        factory = { webView },
                        modifier = if (contentHeight < 700) {
                            Modifier
                                .fillMaxSize()
                                .weight(1f)
                        } else {
                            Modifier
                                .fillMaxSize()

                        }
                    )
                }
                //PULL-TO-REFRESH
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    contentColor = Color(0xffae52de),
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                if (isRefreshing) {
                    Text(
                        text = "Pull to refresh",
                        color = Color(0xffae52de),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 60.dp)
                    )
                }
            }
        }
        LaunchedEffect(webView) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    isRefreshing = false
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WebViewPage(url: String) {
    val context = LocalContext.current

    var isRefreshing by remember { mutableStateOf(false) }
    var LinearProgressIndicatorProgress by remember { mutableFloatStateOf(0f) }
    var contentHeight by remember { mutableIntStateOf(0) }

    // Remember the WebView instance
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            setBackgroundColor(0x000000)
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    LinearProgressIndicatorProgress = newProgress / 100f
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    isRefreshing = false
                    view?.evaluateJavascript(
                        "(function() { return document.body.scrollHeight; })();"
                    ) { height ->
                        height?.toIntOrNull()?.let {
                            contentHeight = it
                            Log.d("WebViewPage", "Content height: $contentHeight")
                        }
                    }
                }
            }
            loadUrl(url)
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            webView.reload()
        }
    )

    // Progress indicator for WebView
    Surface(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalContext provides context) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (LinearProgressIndicatorProgress < 1f) {
                        LinearProgressIndicator(
                            progress = LinearProgressIndicatorProgress,
                            color = Color(0xffae52de),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AndroidView(
                        factory = { webView },
                        modifier = if (contentHeight < 700) {
                            Modifier
                                .fillMaxSize()
                                .weight(1f)
                        } else {
                            Modifier.fillMaxSize()
                        }
                    )
                }
                // Pull-to-refresh indicator
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    contentColor = Color(0xffae52de),
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                if (isRefreshing) {
                    Text(
                        text = "Pull to refresh",
                        color = Color(0xffae52de),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 60.dp)
                    )
                }
            }
        }
    }
}


private var LinearProgressIndicatorProgress by mutableFloatStateOf(0f)