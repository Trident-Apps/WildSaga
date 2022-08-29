package tv.fipe.f

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import tv.fipe.f.utils.Constants

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        OneSignal.setAppId(Constants.ONESIGNAL_ID)
        OneSignal.initWithContext(applicationContext)
    }

    companion object {
        lateinit var gadId: String
    }
}