package com.thell.mutenotification

import android.app.Application
import android.content.Intent
import android.util.Log
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.services.MuteNotificationListenerService
import com.thell.mutenotification.services.MuteNotificationTileService

class MuteNotificationApplication : Application()
{
    private lateinit var uncaughtExceptionHandler:  Thread.UncaughtExceptionHandler

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(handler)
        init()
    }

    private var  handler = Thread.UncaughtExceptionHandler { p0, p1 ->
        if(p1.message != null)
            Log.e("UncaughtException",p1.message!!)

        uncaughtExceptionHandler.uncaughtException(p0, p1);
    };

    private fun init()
    {
        NotificationServiceHelper.muteNotificationService = Intent(this, MuteNotificationListenerService::class.java)
        val muteNotificationTileService = Intent(this, MuteNotificationTileService::class.java)
        this.startService(muteNotificationTileService)
    }
}

