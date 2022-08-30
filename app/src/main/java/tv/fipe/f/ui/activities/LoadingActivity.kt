package tv.fipe.f.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import tv.fipe.f.MyApplication
import tv.fipe.f.R
import tv.fipe.f.ui.viewmodel.MyViewModel
import tv.fipe.f.utils.Checkers

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels()
    private val checker = Checkers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        lifecycleScope.launch(Dispatchers.IO) {
            MyApplication.gadId =
                AdvertisingIdClient.getAdvertisingIdInfo(applicationContext).id.toString()
            OneSignal.setExternalUserId(MyApplication.gadId)
        }

//        if (checker.isDeviceSecured(this@LoadingActivity)) {
//            startGame()
//        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                with(viewModel.getUrlFromDb()) {
                    if (this == null) {
                        viewModel.fetchDeeplink(this@LoadingActivity)
                            withContext(Dispatchers.Main) {
                                viewModel.urlLveData.observe(this@LoadingActivity) { url ->
                                    Timber.tag("stt").d("start from deeplink")
                                    startWebView(url)
                                }                            }
                    } else {
                        this.url?.let { startWebView(it) }
                        Timber.tag("stt").d("start from db " + this)
                    }
                }
            }
//        }
    }

    private fun startGame() {
        with(Intent(this@LoadingActivity, GameActivity::class.java)) {
            startActivity(this)
            this@LoadingActivity.finish()
        }
    }

    private fun startWebView(url: String) {
        Log.d("stt","start from db $url")


        with(Intent(this@LoadingActivity, WebViewActivity::class.java)) {
            this.putExtra("url", url)
            startActivity(this)
            this@LoadingActivity.finish()
        }
    }
}