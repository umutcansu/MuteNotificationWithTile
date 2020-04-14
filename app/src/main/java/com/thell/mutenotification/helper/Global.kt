package com.thell.mutenotification.helper

import android.content.Context
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.helper.mutestate.MuteStateMemoryAction

class Global
{
    companion object
    {
        const  val  PERMISSION_STATE_KEY = "REQUEST_PERMISSION"
        const  val  NOTIFICATION_PERMISSION_REQUEST_CODE = 100
        const  val  BOOT_PERMISSION_REQUEST_CODE = 200
        const  val  NotificationServiceBroadcastReceiver = "broadcastreceiver.NotificationServiceBroadcastReceiver"
        const val   FILE_NAME = "PREF_FILE"
        const val   MUTE_STATE_KEY = "STATE"

        private var MuteStateAction : IMuteStateAction? = null

        fun getMuteStateAction(context: Context): IMuteStateAction
        {
            if(MuteStateAction == null)
            {
                MuteStateAction = MuteStateMemoryAction(context)
            }
            return MuteStateAction!!
        }


    }
}