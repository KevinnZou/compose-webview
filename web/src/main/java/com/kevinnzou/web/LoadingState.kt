package com.kevinnzou.web

/**
 * Sealed class for constraining possible loading states.
 * See [Loading] and [Finished].
 */
public sealed class LoadingState {
    /**
     * Describes a WebView that has not yet loaded for the first time.
     */
    public data object Initializing : LoadingState()

    /**
     * Describes a webview between `onPageStarted` and `onPageFinished` events, contains a
     * [progress] property which is updated by the webview.
     */
    public data class Loading(val progress: Float) : LoadingState()

    /**
     * Describes a webview that has finished loading content.
     */
    public data object Finished : LoadingState()
}