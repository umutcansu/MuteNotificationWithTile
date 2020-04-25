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


        const  val  NOTIFICATION_PERMISSION_REQUEST_CODE = 100
        const  val  BOOT_PERMISSION_REQUEST_CODE = 200
        const  val  SYSTEM_ALERT_REQUEST_CODE = 300

        const  val  NotificationServiceBroadcastReceiver = "broadcastreceiver.NotificationServiceBroadcastReceiver"
        const  val  TimerBroadcastReceiver = "broadcastreceiver.TimerBroadcastReceiver"

        const val   TimerCreatedFlag = "TimerCreated"
        const val   TimerSetFlag = "TimerSetFlag"
        const val   TimerFinishedFlag = "TimerFinishedFlag"
        const val   TimerCanceledFlag = "TimerCanceledFlag"

        const val   TimerEntity = "TimerEntity"

        const val   FILE_NAME = "PREF_FILE"
        const val   MUTE_STATE_KEY = "STATE"

        const val   VERSION = "1.1.2"
        const val   DATABASE_NAME = "AppDatabase"
        const val   DATABASE_VERSION = 14
        const val   DEBUG = true

        fun startApplication(context: Context)
        {
            var service = Intent(context,TimerIntentService::class.java)
            context.startService(service)
            NotificationServiceHelper.muteNotificationService = Intent(context, MuteNotificationListenerService::class.java)
            val muteNotificationTileService = Intent(context, MuteNotificationTileService::class.java)
            context.startService(muteNotificationTileService)
            SettingsHelper.seedDatabaseValue(context)
            TimerHelper.loadTimer(context)
        }

    }
}