package com.example.ricardoogliari.helloworldandroidthings

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus



class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("message", "From: " + remoteMessage?.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage?.data?.size!! > 0) {
            Log.d("message", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            EventBus.getDefault().post(MessageEvent(remoteMessage?.notification?.getBody()))
            Log.d("message", "Message Notification Body: " + remoteMessage?.notification?.getBody());
        }
    }

}