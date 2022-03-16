package com.example.three_we_mobile.ui.movies

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.three_we_mobile.R
import com.example.three_we_mobile.listener.MainActivityListener
import com.example.three_we_mobile.utils.ConnectionLiveData
import com.example.three_we_mobile.utils.MOVIE_LINK
import com.example.three_we_mobile.utils.MyWebClient


class Movies : Fragment() {

    companion object {
        fun newInstance() = Movies()
    }
    private lateinit var viewModel: MoviesViewModel
    private lateinit var fragView: View
    lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var contentLoadingProgressBar: ContentLoadingProgressBar
    private lateinit var fullFrameLayout: FrameLayout
    private lateinit var mainLinearLayout: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var noInternetLayout: LinearLayout
    private lateinit var btnRefreshConnection: Button

    lateinit var mainActivityListener: MainActivityListener

    /**WEBVIEW PROPERTIES**/
    private lateinit var webView: WebView
    lateinit var fullScreen: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        connectionLiveData = ConnectionLiveData(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragView =  inflater.inflate(R.layout.movie_fragment, container, false)
        initialize()
        appState(savedInstanceState)
        connectivity()
        setupWebView()
        backPress()
        setupSwipeReload()
        setupBtnRefresh()

        return fragView
    }

    private fun appState(savedInstanceState: Bundle?){
        if (savedInstanceState != null){
            webView.restoreState(savedInstanceState)
        }else{
            webView.loadUrl(MOVIE_LINK)
        }
    }
    private fun initialize(){
        webView = fragView.findViewById(R.id.movie_webView)
        contentLoadingProgressBar = fragView.findViewById(R.id.loading_bar)
        fullFrameLayout = fragView.findViewById(R.id.full_screen)
        mainLinearLayout = fragView.findViewById(R.id.main_container)
        swipeRefreshLayout = fragView.findViewById(R.id.swipe_refresh)
        noInternetLayout = fragView.findViewById(R.id.no_internet)
        btnRefreshConnection = fragView.findViewById(R.id.btn_refresh)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
    }

    private fun setupSwipeReload(){
        swipeRefreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    private fun setupBtnRefresh(){
        btnRefreshConnection.setOnClickListener {
            webView.visibility = View.GONE
            // linearLayout.isVisible = false
            noInternetLayout.visibility = View.GONE
            webView.loadUrl(MOVIE_LINK)
        }
    }

    private fun connectivity(){
        connectionLiveData.observe(requireActivity()){
            if (it){
                webView.visibility = View.GONE
               // linearLayout.isVisible = false
                noInternetLayout.visibility = View.GONE
                webView.reload()
            }else{
                webView.isVisible = false
              //  linearLayout.isVisible = true
                noInternetLayout.visibility = View.VISIBLE
            }
        }
    }
    private fun setupWebView(){
        setupWebClient()
        setupWebChrmeClient()
    }
    private fun setupWebChrmeClient(){
        webView.webChromeClient = object : WebChromeClient(){

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                if (view is FrameLayout){
                    fullScreen = view
                    fullFrameLayout.addView(fullScreen)
                    fullFrameLayout.visibility = View.VISIBLE
                    contentLoadingProgressBar.visibility = View.GONE
                    mainLinearLayout.visibility = View.GONE
                    mainActivityListener.onFullScreen(false)
                }
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                fullFrameLayout.removeView(fullScreen)
                fullFrameLayout.visibility = View.GONE
                contentLoadingProgressBar.visibility = View.VISIBLE
                mainLinearLayout.visibility = View.VISIBLE
                mainActivityListener.onFullScreen(true)
            }

            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
                webView.goBack()
            }
        }
    }
    private fun setupWebClient(){
        webView.webViewClient = MyWebClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                contentLoadingProgressBar.isVisible = false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                contentLoadingProgressBar.isVisible = true
                swipeRefreshLayout.isRefreshing = false
                webView.visibility = View.VISIBLE
                noInternetLayout.visibility = View.GONE

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {

                if (error?.description != "net::ERR_INTERNET_DISCONNECTED" &&
                    error?.description != "net::ERR_CONNECTION_ABORTED"  ){
                }else{
                    webView.visibility = View.GONE
                    noInternetLayout.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun backPress(){
        webView.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
                        webView.goBack()
                        return@setOnKeyListener true
                    }
                }
            }
            return@setOnKeyListener false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }


}