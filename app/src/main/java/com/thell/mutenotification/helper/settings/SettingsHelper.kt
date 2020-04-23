package com.thell.mutenotification.helper.settings

import com.thell.mutenotification.database.entity.SettingsEntity

class SettingsHelper
{
    companion object
    {
        var   IS_NOTIFICATION_SAVED_ALWAYS = SettingsStateType.NOK
        var   IS_MUTE_NOTIFICATION_TOAST  = SettingsStateType.OK

        const val   SETTINGS_KEY_IS_NOTIFICATION_SAVED_ALWAYS = "Would you like to save notifications to history while notification are allowed?"
        const val   SETTINGS_KEY_IS_MUTE_NOTIFICATION_TOAST = "Would you like to toast messages?"

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

    }
}