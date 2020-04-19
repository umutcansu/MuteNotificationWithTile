package com.thell.mutenotification.model

import com.thell.mutenotification.R


data class NavigationDrawerItem(var title: String, var icon: Int) {

    companion object {

        var SETTING = "Ayarlar"

        fun allMenuItem(): ArrayList<NavigationDrawerItem> {
            val list = ArrayList<NavigationDrawerItem>()

            list.add(
                NavigationDrawerItem(
                    SETTING,
                    R.drawable.ic_settings_black_24dp
                )
            )


            return  list
        }
    }

}