package com.korashiGroup.salamtakPatient.notification

import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.korashiGroup.salamtakPatient.R
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
        //   Log.e(TAG, "data: ${message.data}")


//        val uri: Uri = Uri.parse(message.notification?.link.toString())
//        val url = uri.toString()
        val x =
            "link=SalamtakPatient://2?token=0068c6a0bb92eb943b28327bb286bd0b03fIAAOoKBCtgBYc!!1Lec0lHlOuG9rjSxmjXTiGegqvDQYBhA!!fbM8AAAAAEACzX8YFbnqVZAEAAQCmNJRk&" +
                    "DoctorName=Mostafa *Ali*aaaaaa&DoctorImage=!!Images!!Doctor!!Profile!!9406c34c-521b-4730-8741-5f23b65d148a.jfif&Channelname=46c8b940-6214-4a6c-9310-f39bc6018ee7&PatientID=151&" +
                    "date=2023-06-22 14:43:46"
        val link =
            "{link=SalamtakPatient://2?token=0068c6a0bb92eb943b28327bb286bd0b03fIAAOoKBCtgBYc!!1Lec0lHlOuG9rjSxmjXTiGegqvDQYBhA!!fbM8AAAAAEACzX8YFbnqVZAEAAQCmNJRk&DoctorName=Mostafa *Ali*aaaaaa&DoctorImage=!!Images!!Doctor!!Profile!!9406c34c-521b-4730-8741-5f23b65d148a.jfif&Channelname=46c8b940-6214-4a6c-9310-f39bc6018ee7&PatientID=151&date=2023-06-22 14:43:46}"

        val value = message.data.values
        val entriess = message.data.entries
        var result = ""
        for (enter in entriess) {
            Log.e(TAG, "enter.value: ${enter.value}")
            result = enter.value
        }

//        for (values in value ){
//            Log.e(TAG, "keys: $values")
//            result=values
//        }


        val map = getQueryString(x)
        Log.e(TAG, "map: $map")


        if (link.contains("SalamtakPatient://2")) {
            linkAudioOrVideo = "audio"
        } else {
            linkAudioOrVideo = "video"
        }

        val deepLinkItem = DeepLinkItem(
            map?.get("token")?.let { getDeepLink(it) }, map?.get("DoctorName"),
            map?.get("DoctorImage")?.let { getDeepLink(it) },
            map?.get("Channelname"), map?.get("date"), linkAudioOrVideo
        )
        Log.e("Element  ", "deepLinkItem: ${deepLinkItem}")

        val uri: Uri = Uri.parse(link)
        val jsonobject = convertUriToJson(uri)

        Log.e(TAG, "jsonobject: ${jsonobject}")
        mNotificationManager.textNotification(
            message.notification?.title.toString(),
            message.notification?.body.toString(),
            deepLinkItem
        )

    }

    fun getQueryString(url: String?): HashMap<String, String>? {
        val uri: Uri = Uri.parse(url)
        val map: HashMap<String, String> = HashMap()
        for (paramName in uri.getQueryParameterNames()) {
            if (paramName != null) {
                val paramValue: String? = uri.getQueryParameter(paramName)
                if (paramValue != null) {
                    map[paramName] = paramValue
                }
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