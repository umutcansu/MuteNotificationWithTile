package com.thell.mutenotification.services

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.helper.database.DatabaseHelper
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.mutestate.MuteStateActionHelper
import com.thell.mutenotification.helper.mutestate.MuteStateSharedPrefAction
import com.thell.mutenotification.helper.notificationservice.NotificationServiceHelper
import com.thell.mutenotification.helper.settings.SettingsHelper
import com.thell.mutenotification.helper.settings.SettingsStateType


class MuteNotificationListenerService : NotificationListenerService()
{

    val TAG = "MuteNotificationListenerService"



    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        NotificationServiceHelper.SERVICE_IS_RUNNIG = true
        registerMuteStateReceiver()
        Log.e(TAG,"onStart")
    }


    override fun onDestroy() {
        super.onDestroy()
        NotificationServiceHelper.SERVICE_IS_RUNNIG = false
        try {
            unregisterReceiver(receiverMuteState)
        } catch (e: Exception) {
        }
        Log.e(TAG,"onDestroy")
    }

    private val receiverMuteState = object : NotificationServiceBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            super.onReceive(p0, p1)
            val state = p1!!.getBooleanExtra(MuteStateSharedPrefAction.MUTE_STATE_KEY,false)
            if(!state)
            {
                if(SettingsHelper.getSaveAlwaysSettingsState().State != SettingsStateType.OK.state)
                {
                    if(NotificationServiceHelper.SERVICE_IS_RUNNIG)
                        onDestroy()
                }
            }

        }
    }

    private fun registerMuteStateReceiver()
    {
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        registerReceiver(receiverMuteState, filter)
    }

    override fun onBind(intent: Intent?): IBinder?
    {
        return super.onBind(intent)
    }

    private fun convertNotificationEntity(sbn:StatusBarNotification):NotificationEntity
    {
        val extras: Bundle = sbn.notification.extras
        val notification = sbn.notification

        return NotificationEntity().apply {
            PackageName = sbn.packageName ?: ""

            @Suppress("DEPRECATION")
            IconId =  extras.getInt(Notification.EXTRA_SMALL_ICON).toString()

            Ticket = if(notification.tickerText == null)
                ""
            else
                notification.tickerText.toString()


            Ticket += " ${notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()}"

            ApplicationName = GuiHelper.getAppNameFromPackageName(
                this@MuteNotificationListenerService,
                PackageName


            )
            MuteState = MuteStateActionHelper.getMuteStateAction(this@MuteNotificationListenerService).getMuteState()
            Category = notification.category ?: ""
            PostTime = sbn.postTime
            NotificationID = sbn.id
        }

    }

    private fun saveNotification(notificationEntity: NotificationEntity)
    {
        Thread{
            val db = DatabaseHelper.getInstance(applicationContext)
            db.getNotificationDao().insert(notificationEntity)
        }.start()
    }

    private fun muteAction(sbn: StatusBarNotification)
    {
        cancelAllNotifications()
        val notificationEntity = convertNotificationEntity(sbn)
        saveNotification(notificationEntity)


        if(SettingsHelper.getMuteSettingsState().State == SettingsStateType.OK.state)
            Toast.makeText(
                this,
                "Push Notification For ${notificationEntity.ApplicationName}",
                    Toast.LENGTH_SHORT
            ).show()
    }

    private fun notificationAction(sbn: StatusBarNotification)
    {
        if(SettingsHelper.getSaveAlwaysSettingsState().State == SettingsStateType.OK.state)
        {
            val notificationEntity = convertNotificationEntity(sbn)
            saveNotification(notificationEntity)
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification)
    {
        if(MuteStateActionHelper.getMuteStateAction(this).getMuteState())
        {
           muteAction(sbn)
        }
        else
        {
            notificationAction(sbn)
        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification)
    {

    }

}