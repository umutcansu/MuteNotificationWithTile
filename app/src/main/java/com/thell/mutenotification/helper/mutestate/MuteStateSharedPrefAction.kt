package com.thell.mutenotification.helper.mutestate

import android.content.Context
import com.thell.mutenotification.MainActivity
import com.thell.mutenotification.helper.Global

class MuteStateSharedPrefAction(context: Context): MuteStateAction(context)
{

    override fun getMuteState(): Boolean {
        val sharedPref = context.getSharedPreferences(Global.FILE_NAME,Context.MODE_PRIVATE)
        return sharedPref.getBoolean(Global.MUTE_STATE_KEY,false)
    }

    override fun setMuteState(value: Boolean) {
        val sharedPref = context.getSharedPreferences(Global.FILE_NAME,Context.MODE_PRIVATE)
        val  editor = sharedPref.edit()
        editor.putBoolean(Global.MUTE_STATE_KEY,value)
        editor.commit()

        sendBroadcast()
    }

}