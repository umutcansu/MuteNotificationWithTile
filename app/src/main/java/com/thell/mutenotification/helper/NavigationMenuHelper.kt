package com.thell.mutenotification.helper

import com.thell.mutenotification.R
import com.thell.mutenotification.model.NavigationDrawerItem

class NavigationMenuHelper
{
    companion object
    {

        var SETTING = "Settings"
        var HISTORY = "History"
        var TIMER = "Timer"
        var HOME = "Home"

        val allMenuItem = arrayListOf(
            NavigationDrawerItem(
                HOME,
                R.drawable.ic_home_black_24dp,
                true
            ),
            NavigationDrawerItem(
                TIMER,
                R.drawable.ic_alarm_black_24dp
            ),
            NavigationDrawerItem(
                SETTING,
                R.drawable.ic_settings_black_24dp
            ),
            NavigationDrawerItem(
                HISTORY,
                R.drawable.ic_history_black_24dp
            )
        )
    }
}