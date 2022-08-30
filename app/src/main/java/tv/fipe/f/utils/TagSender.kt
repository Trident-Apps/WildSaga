package tv.fipe.f.utils

import com.onesignal.OneSignal

class TagSender {

    fun sendTag(deeplink: String, data: MutableMap<String, Any>?) {
        val campaign = data?.get("campaign").toString()

        if (campaign == "null" && deeplink == "null") {
            OneSignal.sendTag("key2", "organic")
        } else if (deeplink != "null") {
            OneSignal.sendTag("key2", deeplink.replace("myapp://", "").substringBefore("/"))
        } else if (campaign != "null") {
            OneSignal.sendTag("key2", campaign.substringBefore("_"))
        }
    }
}