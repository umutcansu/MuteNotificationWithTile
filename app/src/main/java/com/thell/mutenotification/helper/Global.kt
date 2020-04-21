package com.thell.mutenotification.helper

import android.content.Context
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.helper.mutestate.MuteStateMemoryAction
import com.thell.mutenotification.helper.mutestate.MuteStateSharedPrefAction

class Global
{
    companion object
    {

        const  val  NOTIFICATION_PERMISSION_REQUEST_CODE = 100
        const  val  BOOT_PERMISSION_REQUEST_CODE = 200
        const  val  SYSTEM_ALERT_REQUEST_CODE = 300
        const  val  NotificationServiceBroadcastReceiver = "broadcastreceiver.NotificationServiceBroadcastReceiver"
        const val   FILE_NAME = "PREF_FILE"
        const val   MUTE_STATE_KEY = "STATE"
        const val   VERSION = "1.0.8"
        const val   DATABASE_NAME = "AppDatabase"
        const val   DEBUG = true




        private var MuteStateAction : IMuteStateAction? = null

        fun getMuteStateAction(context: Context): IMuteStateAction
        {
            if(MuteStateAction == null)
            {
                MuteStateAction = MuteStateSharedPrefAction(context)
            }
            return MuteStateAction!!
        }


    }
}