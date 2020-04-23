package com.thell.mutenotification

import android.app.Application
import android.content.Intent
import android.util.Log
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.helper.database.DatabaseHelper
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.notificationservice.NotificationServiceHelper
import com.thell.mutenotification.helper.settings.SettingsHelper
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
        seedDatabaseValue()
    }

    private fun seedDatabaseValue()
    {
        val settingsDao =  DatabaseHelper.getInstance(this).getSettingsDao()

        val muteToastSettingsKey = SettingsHelper.Companion::SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST.name
        val alwaysSaveSettingsKey = SettingsHelper.Companion::SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS.name

        val muteToast = settingsDao.getBySettingsKey(muteToastSettingsKey)
        val alwaysSave = settingsDao.getBySettingsKey(alwaysSaveSettingsKey)

        if(muteToast == null)
        {
            settingsDao.insert(
                SettingsHelper.allSettingsList.first { it.SettingsKey == muteToastSettingsKey }
            )
        }
        else
        {
            SettingsHelper.setSettingsState(muteToastSettingsKey,muteToast.State)
        }

        if(alwaysSave == null)
        {
            settingsDao.insert(
                SettingsHelper.allSettingsList.first { it.SettingsKey == alwaysSaveSettingsKey }
            )
        }
        else
        {
            SettingsHelper.setSettingsState(alwaysSaveSettingsKey,alwaysSave.State)
        }
    }
}

