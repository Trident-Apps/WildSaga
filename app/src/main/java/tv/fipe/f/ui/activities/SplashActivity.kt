package tv.fipe.f.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tv.fipe.f.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        with(Intent(this@SplashActivity, LoadingActivity::class.java)) {
            startActivity(this)
            this@SplashActivity.finish()
        }
    }
}