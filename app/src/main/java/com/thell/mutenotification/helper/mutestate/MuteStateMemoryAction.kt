package com.thell.mutenotification.helper.mutestate

import android.content.Context

class MuteStateMemoryAction(context: Context): MuteStateAction(context)
{
    companion object
    {
        var STATE = false
    }

    override fun getMuteState(): Boolean
    {
        return STATE
    }

    override fun setMuteState(value: Boolean)
    {
        STATE = value
        sendBroadcast()
    }
}


