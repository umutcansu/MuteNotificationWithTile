package com.thell.mutenotification.helper

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import com.thell.mutenotification.MainActivity
import com.thell.mutenotification.R

class NotificationServiceHelper
{
    companion object
    {
        var muteNotificationService: Intent? = null

        fun start(context: Context)
        {

            if(!PermissionHelper.isNotificationServiceEnabled(context))
            {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            else
            {
                startNotificationListenerService(context)
            }
        }

        fun stop(context: Context)
        {
            stopNotificationListenerService(context)
        }


        fun startNotificationListenerService(context: Context)
        {
            if(muteNotificationService != null)
            {

                context.startService(muteNotificationService!!)
            }

        }

        fun stopNotificationListenerService(context: Context)
        {
            if(muteNotificationService != null)
            {

                context.stopService(muteNotificationService)
            }

        }


    }
}