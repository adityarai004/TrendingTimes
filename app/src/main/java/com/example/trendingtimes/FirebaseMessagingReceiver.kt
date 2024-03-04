package com.example.trendingtimes

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.trendingtimes.ui.activity.FeedbackActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessagingReceiver : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("TAG","Token $token")
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.getNotification()!!.title
        val text = remoteMessage.getNotification()!!.body

        Log.d("TAG","Text is $text")
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"

        val resultIntent = Intent(this, FeedbackActivity::class.java)
        resultIntent.putExtra("extra_url",text)
        val resultPendingIntent: PendingIntent? = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.done_icon)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        with(NotificationManagerCompat.from(this)){
            notify(1,builder.build())
        }
        super.onMessageReceived(remoteMessage)
    }
}
