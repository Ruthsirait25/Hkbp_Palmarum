package com.example.hkbptarutung.utils

import android.R.id
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hkbptarutung.R
import com.example.hkbptarutung.SplashScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class PushNotification : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseUtils.setToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println("dapet nih ${message.messageId}")
        val title = message.notification?.title ?: ""
        val msg = message.notification?.body ?: ""
        handleNotif(title, msg)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.registrasi)
            val descriptionText = getString(R.string.registrasi_sidi)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(name, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun handleIntent(intent: Intent?) {
        try {
            if (intent!!.extras != null) {
                val builder = RemoteMessage.Builder("FirebaseMessageReceiver")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }

    private fun handleNotif(title: String, message: String) {
        createChannel()
        val notificationIntent = Intent(this, SplashScreen::class.java)
        notificationIntent.putExtra("text", id.message)
        notificationIntent.putExtra("title", title)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val notificationBuilder =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder = NotificationCompat.Builder(applicationContext, getString(R.string.registrasi))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder = builder.setContent()
        } else {
            builder = builder.setContentTitle(title)
                .setContentText(message)
//                .setSmallIcon(R.drawable.favicon)
        }

        var notificationId = 1
        if (notificationId == Int.MAX_VALUE - 1) notificationId = 0
        notificationBuilder.notify(notificationId, builder.build())
    }

//    private fun getCustomDesign(title: String, message: String): RemoteViews? {
//        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notification)
//        remoteViews.setTextViewText(id.title111, title)
//        remoteViews.setTextViewText(id.message111, message)
//        remoteViews.setImageViewResource(id.icon111, R.drawable.favicon)
//        return remoteViews
//    }
}

val key =
    "AAAAdOllyuE:APA91bEol6wviNxepGiXqTH-sq6E5RGeQDfpiEcRrmOjbuvXiQsWfG3p6OTWAT7NAVkulp-N0DGt7ufRKRrjcs-y0eAvxXI5CjOq05ZqRsLaaU6mnQgOh-jCYA4FEQ9ii-MT9lQlrnZd"

fun sendSimpleNotif(token: String, msg: String) {
    sendPushNotification(
        token, msg, "", "", emptyMap()
    )
}

fun sendPushNotification(
    token: String,
    title: String,
    subtitle: String,
    body: String,
    data: Map<String, String> = emptyMap()
) {
    val url = "https://fcm.googleapis.com/fcm/send"

    val bodyJson = JSONObject()
    bodyJson.put("to", token)
    bodyJson.put("notification",
        JSONObject().also {
            it.put("title", title)
            it.put("subtitle", subtitle)
            it.put("body", body)
            it.put("sound", "social_notification_sound.wav")
        }
    )
    bodyJson.put("data", JSONObject(data))

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "key=$key")
        .post(
            bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        )
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(
        object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Received data: ${response.body?.string()}")
            }

            override fun onFailure(call: Call, e: IOException) {
                println("error ${e.message}")
            }
        }
    )
}