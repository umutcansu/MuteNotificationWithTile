package com.thell.mutenotification.helper.notificationservice

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.thell.mutenotification.MainActivity
import com.thell.mutenotification.helper.mutestate.MuteStateAction
import com.thell.mutenotification.helper.mutestate.MuteStateActionHelper
import com.thell.mutenotification.helper.permission.PermissionHelper
import com.thell.mutenotification.helper.settings.SettingsHelper
import com.thell.mutenotification.helper.settings.SettingsStateType

class NotificationServiceHelper private constructor()
{
    companion object
    {
        var muteNotificationService: Intent? = null

        var SERVICE_IS_RUNNIG = false

        fun setStateService(context: Context,state:Boolean)
        {
            if(!SERVICE_IS_RUNNIG)
            {
                if(state)
                {
                    if (MuteStateActionHelper.getMuteStateAction(context).getMuteState()) {
                        start(context)
                    }
                }
            }

        }

        private fun start(context: Context)
        {

            if(!PermissionHelper.isNotificationServiceEnabled(context))
            {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            else
            {
                startNotificationListenerService(
                    context
                )
            }
        }


        private fun startNotificationListenerService(context: Context)
        {
            if(muteNotificationService != null)
            {
                context.startService(muteNotificationService)
            }

        }


    }
}