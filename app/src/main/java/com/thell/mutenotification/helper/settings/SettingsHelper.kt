package com.thell.mutenotification.helper.settings

import android.content.Context
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.helper.database.DatabaseHelper

class SettingsHelper private constructor()
{
    companion object
    {
        var   IS_NOTIFICATION_SAVED_ALWAYS = SettingsStateType.NOK
        var   IS_MUTE_NOTIFICATION_TOAST  = SettingsStateType.OK

        const val   SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS = "Would you like to save notifications to history  while notifications are allowed?\nWarning : it may drain your phone battery"
        const val   SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST = "Would you like to receive toast messages?"

        val allSettingsList = arrayListOf(
            SettingsEntity(
                SettingsKey =  Companion::SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS.name,
                SettingsDescription =  SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS,
                State = IS_NOTIFICATION_SAVED_ALWAYS.state
            ),
            SettingsEntity(
                SettingsKey =  Companion::SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST.name,
                SettingsDescription =  SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST,
                State = IS_MUTE_NOTIFICATION_TOAST.state
            )
        )

        fun setSettingsState(settingsKey:String,state:Int)
        {
            allSettingsList.first { it.SettingsKey == settingsKey }.State = state
        }

        fun getSettingsState(settingsKey:String): SettingsEntity
        {
            return allSettingsList.first { it.SettingsKey == settingsKey }
        }

        fun getSaveAlwaysSettingsState(): SettingsEntity
        {
            return getSettingsState(Companion::SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS.name)
        }

        fun getMuteSettingsState(): SettingsEntity
        {
            return getSettingsState(Companion::SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST.name)
        }

        fun seedDatabaseValue(context: Context)
        {
            val settingsDao =  DatabaseHelper.getInstance(context).getSettingsDao()

            val muteToastSettingsKey = SettingsHelper.Companion::SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST.name
            val alwaysSaveSettingsKey = SettingsHelper.Companion::SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS.name

            val muteToast = settingsDao.getBySettingsKey(muteToastSettingsKey)
            val alwaysSave = settingsDao.getBySettingsKey(alwaysSaveSettingsKey)

            if(muteToast == null)
            {
                settingsDao.insert(
                    allSettingsList.first { it.SettingsKey == muteToastSettingsKey }
                )
            }
            else
            {
                setSettingsState(muteToastSettingsKey,muteToast.State)
            }

            if(alwaysSave == null)
            {
                settingsDao.insert(
                    allSettingsList.first { it.SettingsKey == alwaysSaveSettingsKey }
                )
            }
            else
            {
                setSettingsState(alwaysSaveSettingsKey,alwaysSave.State)
            }
        }

    }
}