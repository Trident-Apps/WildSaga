package tv.fipe.f.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import tv.fipe.f.R
import tv.fipe.f.db.UrlEntity
import tv.fipe.f.ui.viewmodel.MyViewModel

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {

    lateinit var webView: WebView
    private val viewModel: MyViewModel by viewModels()
    private var messageAB: ValueCallback<Array<Uri?>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.web_view)
        intent.getStringExtra(INTENT_EXTRA_NAME)?.let { webView.loadUrl(it) }
        webView.webViewClient = LocalClient()
        webView.settings.userAgentString = System.getProperty(SYSTEM_PROPERTY)
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = false
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAB = filePathCallback
                selectImageIfNeeded()
                return true
            }

            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(this@WebViewActivity)
                newWebView.webChromeClient = this
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        view!!.loadUrl(url!!)
                        return true
                    }
                }
                return true
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            messageAB?.onReceiveValue(null)
            return
        } else if (resultCode == Activity.RESULT_OK) {
            if (messageAB == null) return

            messageAB!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode, data
                )
            )
            messageAB = null
        }
    }

    private fun selectImageIfNeeded() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = INTENT_TYPE
        startActivityForResult(Intent.createChooser(intent, CHOOSER_TITLE), RESULT_CODE)
    }

    private inner class LocalClient : WebViewClient() {

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (url == BASE_URL) {
                with(Intent(this@WebViewActivity, GameActivity::class.java)) {
                    startActivity(this)
                    this@WebViewActivity.finish()
                }
            } else {
                viewModel.getUrlFromDb().observe(this@WebViewActivity) {
                    if (it == null) {
                        viewModel.insertUrlToDB(UrlEntity(url = url))
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }

    companion object {
        const val INTENT_EXTRA_NAME = "url"
        const val INTENT_TYPE = "image/*"
        const val CHOOSER_TITLE = "Image Chooser"
        const val BASE_URL = "https://wildsaga.space/"
        const val RESULT_CODE = 1
        const val SYSTEM_PROPERTY = "http.agent"
    }
}