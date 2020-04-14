package com.thell.mutenotification.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.services.MuteNotificationListenerService

class BootBroadcastReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        NotificationServiceHelper.muteNotificationService = Intent(context,MuteNotificationListenerService::class.java)
        NotificationServiceHelper.start(context)
    }

}