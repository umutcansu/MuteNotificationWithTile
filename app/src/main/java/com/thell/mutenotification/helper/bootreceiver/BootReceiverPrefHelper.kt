package com.thell.mutenotification.helper.bootreceiver

import android.content.Context

class BootReceiverPrefHelper private constructor()
{
    companion object
    {

        private const val FILE_NAME = "AUTOSTART_PREF_FILE"
        private const val AUTOSTART_KEY = "AUTOSTART_KEY"

        fun writeBoolean(context: Context, state:Boolean)
        {
            val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val  editor = sharedPref.edit()
            editor.putBoolean(AUTOSTART_KEY,state)
            editor.apply()

        }

        fun readBoolean(context: Context):Boolean
        {
            val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

            return sharedPref.getBoolean(AUTOSTART_KEY,false)

        }


    }
}