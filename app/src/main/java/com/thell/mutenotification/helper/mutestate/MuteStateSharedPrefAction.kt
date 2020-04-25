package com.thell.mutenotification.helper.mutestate

import android.content.Context
import com.thell.mutenotification.helper.Global

class MuteStateSharedPrefAction(context: Context): MuteStateAction(context)
{

    companion object
    {
        const val   FILE_NAME = "PREF_FILE"
        const val   MUTE_STATE_KEY = "STATE"
    }


    override fun getMuteState(): Boolean {
        val sharedPref = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MUTE_STATE_KEY,false)
    }

    override fun setMuteState(value: Boolean) {
        val sharedPref = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
        val  editor = sharedPref.edit()
        editor.putBoolean(MUTE_STATE_KEY,value)
        editor.commit()

        sendBroadcast()
    }

}