package com.thell.mutenotification.helper

import android.content.Context
import android.content.Intent
import com.thell.mutenotification.broadcastreceiver.TimerBroadcastReceiver
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.helper.mutestate.MuteStateSharedPrefAction
import com.thell.mutenotification.helper.notificationservice.NotificationServiceHelper
import com.thell.mutenotification.helper.settings.SettingsHelper
import com.thell.mutenotification.helper.timer.TimerHelper
import com.thell.mutenotification.services.MuteNotificationListenerService
import com.thell.mutenotification.services.MuteNotificationTileService
import com.thell.mutenotification.services.TimerIntentService

class Global private constructor()
{

    companion object
    {

        const  val  PERIOD = 10L
        const  val  NOTIFICATION_PERMISSION_REQUEST_CODE = 100
        const  val  BOOT_PERMISSION_REQUEST_CODE = 200
        const  val  SYSTEM_ALERT_REQUEST_CODE = 300

        const  val  NotificationServiceBroadcastReceiver = "broadcastreceiver.NotificationServiceBroadcastReceiver"
        const  val  TimerBroadcastReceiver = "broadcastreceiver.TimerBroadcastReceiver"

        const val   VERSION = "1.1.5"
        const val   DATABASE_NAME = "AppDatabase"
        const val   DATABASE_VERSION = 14

        const val   DEBUG = true
        var START = false

        fun startTimerService(context: Context)
        {
            var service = Intent(context,TimerIntentService::class.java)
            context.startService(service)
        }

        private fun startTileService(context: Context)
        {
            val muteNotificationTileService = Intent(context, MuteNotificationTileService::class.java)
            context.startService(muteNotificationTileService)
        }

        fun startApplication(context: Context)
        {
            if(START)
                return

            START = false


            startTimerService(context)
            NotificationServiceHelper.muteNotificationService = Intent(context, MuteNotificationListenerService::class.java)
            NotificationServiceHelper.setStateService(context!!,true)
            startTileService(context)
            SettingsHelper.seedDatabaseValue(context)
            TimerHelper.loadTimer(context)
        }

    }
}