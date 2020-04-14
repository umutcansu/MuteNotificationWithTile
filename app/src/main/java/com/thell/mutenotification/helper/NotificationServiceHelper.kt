package com.thell.mutenotification.helper

import android.content.Context
import android.content.Intent

class NotificationServiceHelper
{
    companion object
    {

        var muteNotificationService: Intent? = null

        fun start(context: Context)
        {
            if (checkPermission(context))
            {
                startNotificationListenerService(context)
            }
            else
            {
                startNotificationListenerService(context)
            }

            //bootReceiverPermission(context)
        }

        fun checkPermission(context: Context):Boolean
        {
            if (!PermissionHelper.isNotificationServiceEnabled(context))
            {
                val enableNotificationListenerAlertDialog = PermissionHelper.buildNotificationServiceAlertDialog(context)
                enableNotificationListenerAlertDialog.show()
                return false
            }
            return true
        }



        fun bootReceiverPermission(context: Context)
        {
            //AutoStartHelper.getInstance().getAutoStartPermission(context);
        }


        fun startNotificationListenerService(context: Context)
        {
            if(muteNotificationService != null)
            {
                Global.getMuteStateAction(context).setMuteState(true)
                context.startService(muteNotificationService!!)
            }

        }

        fun stopNotificationListenerService(context: Context)
        {
            if(muteNotificationService != null)
            {
                Global.getMuteStateAction(context).setMuteState(false)
                context.stopService(muteNotificationService)
            }

        }
    }
}