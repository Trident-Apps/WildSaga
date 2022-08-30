package tv.fipe.f.ui.viewmodel

import android.app.Activity
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tv.fipe.f.MyApplication
import tv.fipe.f.db.UrlEntity
import tv.fipe.f.repositories.MyRepositoryInt
import tv.fipe.f.utils.Constants
import tv.fipe.f.utils.TagSender
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepositoryInt
) : ViewModel() {
    private val tagSender = TagSender()

    val urlLveData: MutableLiveData<String> = MutableLiveData()

    fun insertUrlToDB(url: UrlEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUrl(url)
    }

    fun getUrlFromDb() = repository.getUrl()

    fun fetchDeeplink(activity: Activity) {
        AppLinkData.fetchDeferredAppLinkData(activity) {
            val deeplink = it?.targetUri.toString()

            if (deeplink == "null") {
                fetchAppsData(activity)
            } else {
                urlLveData.postValue(createUrl(deeplink, null, activity))
                tagSender.sendTag(deeplink, null)
            }
        }
    }

    private fun fetchAppsData(activity: Activity) {
        AppsFlyerLib.getInstance()
            .init(Constants.APPS_DEV_KEY, object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    tagSender.sendTag("null", data)
                    urlLveData.postValue(createUrl("null", data, activity))
                }

                override fun onConversionDataFail(data: String?) {
                }

                override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                }

                override fun onAttributionFailure(data: String?) {
                }

            }, activity)
        AppsFlyerLib.getInstance().start(activity)
    }


    fun createUrl(
        deeplink: String,
        data: MutableMap<String, Any>?,
        activity: Activity
    ): String {
        val gadId = MyApplication.gadId

        val url = Constants.BASE_URL.toUri().buildUpon().apply {
            appendQueryParameter(Constants.SECURE_GET_PARAMETR, Constants.SECURE_KEY)
            appendQueryParameter(Constants.DEV_TMZ_KEY, TimeZone.getDefault().id)
            appendQueryParameter(Constants.GADID_KEY, gadId)
            appendQueryParameter(Constants.DEEPLINK_KEY, deeplink)
            appendQueryParameter(Constants.SOURCE_KEY, data?.get("media_source").toString())
            if (deeplink == "null") {
                appendQueryParameter(
                    Constants.AF_ID_KEY,
                    AppsFlyerLib.getInstance().getAppsFlyerUID(activity)
                )
            } else {
                appendQueryParameter(Constants.AF_ID_KEY, "null")
            }
            appendQueryParameter(Constants.ADSET_ID_KEY, data?.get(DATA_ADSET_ID).toString())
            appendQueryParameter(Constants.CAMPAIGN_ID_KEY, data?.get(DATA_CAMPAIGN_ID).toString())
            appendQueryParameter(Constants.APP_COMPAIGN_KEY, data?.get(DATA_CAMPAIGN).toString())
            appendQueryParameter(Constants.ADSET_KEY, data?.get(DATA_ADSET).toString())
            appendQueryParameter(Constants.ADGROUP_KEY, data?.get(DATA_ADGROUP).toString())
            appendQueryParameter(Constants.ORIG_COST_KEY, data?.get(DATA_ORIG_COST).toString())
            appendQueryParameter(Constants.AF_SITEID_KEY, data?.get(DATA_AF_SITEID).toString())

        }.toString()
        return url

    }

    companion object {
        val DATA_ADSET_ID = "adset_id"
        val DATA_CAMPAIGN_ID = "campaign_id"
        val DATA_CAMPAIGN = "campaign"
        val DATA_ADSET = "adset"
        val DATA_ADGROUP = "adgroup"
        val DATA_ORIG_COST = "orig_cost"
        val DATA_AF_SITEID = "af_siteid"
    }
}