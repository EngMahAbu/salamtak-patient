package com.korashiGroup.salamtakPatient.notification

import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.model.faweryjson.Fawery
import com.korashiGroup.salamtakPatient.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingServ"
    private var linkAudioOrVideo = ""

    @Inject
    lateinit var mNotificationManager: MyNotificationManager
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("fcm", "Refreshed token: $token")
        DateUtils.setFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)



        val data = message.data

        Log.e(TAG, "FCM Data: $data")

        if (data.isNullOrEmpty()) {
            Log.e(TAG, "Message data is empty")
            return
        }

        val linkData = data["link"]  // الرابط الطويل من FCM
        val map = getQueryString(linkData)

        val linkAudioOrVideo = if (linkData?.contains("SalamtakPatient://4?") == true) {
            "audio"
        } else {
            "video"
        }

        val deepLinkItem = DeepLinkItem(
            token = map["token"]?.let { getDeepLink(it) },
            DoctorName = map["DoctorName"]?.replace("*", " "), // استبدل * بمسافة
            DoctorImage = map["DoctorImage"]?.let { getDeepLink(it) },
            Channelname = map["Channelname"],
            date = map["date"],
            link = linkAudioOrVideo
        )
        Log.e(TAG, "deepLinkItem: $deepLinkItem")

        // عرض الإشعار
        mNotificationManager.textNotification(
            message.notification?.title.orEmpty(),
            message.notification?.body.orEmpty(),
            deepLinkItem
        )

    }

    fun getQueryString(url: String?): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        if (url == null) return map

        val uri = Uri.parse(url)
        for (paramName in uri.queryParameterNames) {
            uri.getQueryParameter(paramName)?.let { value ->
                map[paramName] = value
            }
        }
        return map
    }



    fun convertUriToJson(uri: Uri): JSONObject {
        val json = JSONObject()

        for (parameter in uri.queryParameterNames) {
            val value = uri.getQueryParameter(parameter)
            json.put(parameter, value)
        }

        return json
    }


    private fun getDeepLink(item: String): String {
        return item.replace("!!", "/")
    }


}