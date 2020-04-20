package com.thell.mutenotification.model

import com.thell.mutenotification.R


data class NavigationDrawerItem(var title: String, var icon: Int,var selected : Boolean = false) {

    companion object {

        var SETTING = "Settings"
        var HISTORY = "History"
        var TIMER = "Timer"
        var HOME = "Home"


        fun allMenuItem(): ArrayList<NavigationDrawerItem> {
            val list = ArrayList<NavigationDrawerItem>()

            list.apply {

                add(
                    NavigationDrawerItem(
                        HOME,
                        R.drawable.ic_home_black_24dp
                    )
                )
                add(
                    NavigationDrawerItem(
                        TIMER,
                        R.drawable.ic_alarm_black_24dp
                    )
                )

                add(
                    NavigationDrawerItem(
                        SETTING,
                        R.drawable.ic_settings_black_24dp
                    )
                )

                add(
                    NavigationDrawerItem(
                        HISTORY,
                        R.drawable.ic_history_black_24dp
                    )
                )
            }





            return  list
        }
    }

}