package com.thell.mutenotification.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast

class MuteNotificationListenerService : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder?
    {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification)
    {


    }

    override fun onNotificationRemoved(sbn: StatusBarNotification)
    {

    }


}