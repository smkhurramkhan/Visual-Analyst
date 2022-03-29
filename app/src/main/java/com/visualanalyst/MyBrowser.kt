package com.visualanalyst

import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by Malik A Pasha on 11/21/2018.
 */
class MyBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}