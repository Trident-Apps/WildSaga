package tv.fipe.f.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        if (!checker.isDeviceSecured(this@LoadingActivity)) {
            startGame()
        } else {

            viewModel.getUrlFromDb().observe(this) {

                if (it == null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.fetchDeeplink(this@LoadingActivity)
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        viewModel.urlLveData.observe(this@LoadingActivity) { url ->
                            Timber.d("start from deeplink")
                            startWebView(url)
                        }
                    }
                } else {
                    viewModel.getUrlFromDb().observe(this@LoadingActivity) { urlEntity ->
                        startWebView(urlEntity.url!!)
                        Timber.d("start from db $urlEntity")
                        Timber.d(urlEntity.toString())
                    }
                }
            }
        }

    }

    private fun startGame() {
        with(Intent(this@LoadingActivity, GameActivity::class.java)) {
            startActivity(this)
            this@LoadingActivity.finish()
        }
    }

    private fun startWebView(url: String) {
        with(Intent(this@LoadingActivity, WebViewActivity::class.java)) {
            this.putExtra("url", url)
            startActivity(this)
            this@LoadingActivity.finish()
        }
    }
}