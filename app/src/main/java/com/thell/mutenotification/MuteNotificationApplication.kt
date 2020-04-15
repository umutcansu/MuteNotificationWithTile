package com.thell.mutenotification

import android.app.Application
import android.content.Intent
import android.util.Log
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.services.MuteNotificationListenerService
import com.thell.mutenotification.services.MuteNotificationTileService

class MuteNotificationApplication : Application()
{

    override fun onCreate() {
        super.onCreate()

        val exceptionHandler = MuteNotificationUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
        NotificationServiceHelper.muteNotificationService = Intent(this, MuteNotificationListenerService::class.java)
        init()

    }

    private fun init()
    {
        var muteNotificationTileService: Intent = Intent(this, MuteNotificationTileService::class.java)
        this.startService(muteNotificationTileService)
    }
}

class MuteNotificationUncaughtExceptionHandler : Thread.UncaughtExceptionHandler
{
    override fun uncaughtException(thread: Thread, exc: Throwable)
    {
        Log.e("UncaughtExceptionHandler",exc.message)
    }
}