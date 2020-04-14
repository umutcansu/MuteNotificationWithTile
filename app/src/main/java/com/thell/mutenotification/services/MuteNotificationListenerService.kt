package com.thell.mutenotification.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast
import com.thell.mutenotification.helper.Global

class MuteNotificationListenerService : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder?
    {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification)
    {
        if(Global.getMuteStateAction(this).getMuteState())
        {
            cancelAllNotifications()
            Toast.makeText(this, "Push Notification", Toast.LENGTH_LONG).show()
        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification)
    {

    }


}