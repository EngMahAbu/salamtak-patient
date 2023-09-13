package com.korashiGroup.salamtakPatient


import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.korashiGroup.salamtakPatient.notification.NotificationApp
import com.korashiGroup.salamtakPatient.utils.DateUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SamlamtakApp : Application(){
    private  val TAG = "MyFirebaseMessagingServ"
    lateinit var notificationApp: NotificationApp
    companion object {
        var instance: SamlamtakApp? = null

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        DateUtils.setup(applicationContext)
        notificationApp = NotificationApp(this)
        NotificationApp.instance?.createChannel(
            getString(R.string.Notification_channel_Id),
            getString(R.string.Notification_channel_Name)
        )
        onTokenRefresh()
    }



    fun onTokenRefresh(){
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
              DateUtils.setFcmToken(token)
//            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    }
    fun ttrigerNotifiction(title: String, contenttext: String) {
        NotificationApp.instance?.triggerNotification(
            getString(R.string.Notification_channel_Id),
            title,
            contenttext
        )
    }


}