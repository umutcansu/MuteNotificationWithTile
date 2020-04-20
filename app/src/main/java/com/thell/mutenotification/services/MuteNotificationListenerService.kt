package com.thell.mutenotification.services

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.thell.mutenotification.database.AppDatabase
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.helper.DatabaseHelper
import com.thell.mutenotification.helper.Global


class MuteNotificationListenerService : NotificationListenerService()
{

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

            IconId =  extras.getInt(Notification.EXTRA_SMALL_ICON).toString()

            Ticket = if(notification.tickerText == null)
                ""
            else
                notification.tickerText.toString()


            Category = notification.category ?: ""
            PostTime = sbn.postTime
            NotificationID = sbn.id
        }

    }

    private fun saveNotification(notificationEntity: NotificationEntity)
    {
        val db = DatabaseHelper.getInstance(applicationContext)
        db.getNotificationDao().insert(notificationEntity)

    }

    private fun muteAction(sbn: StatusBarNotification)
    {
        cancelAllNotifications()
        val notificationEntity = convertNotificationEntity(sbn)
        saveNotification(notificationEntity)

        if(Global.DEBUG)
            Toast.makeText(
                this,
                "Push Notification ${notificationEntity.PackageName}",
                    Toast.LENGTH_SHORT
            ).show()
    }



    private fun notificationAction(sbn: StatusBarNotification)
    {

    }

    override fun onNotificationPosted(sbn: StatusBarNotification)
    {
        if(Global.getMuteStateAction(this).getMuteState())
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