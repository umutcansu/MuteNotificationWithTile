package com.thell.mutenotification.helper.mutestate

import android.content.Context
import android.content.Intent
import com.thell.mutenotification.helper.Global


abstract class MuteStateAction(val context: Context):IMuteStateAction
{

    abstract override fun getMuteState():Boolean

    abstract override fun setMuteState(value: Boolean)

    override fun sendBroadcast()
    {
        val intent =  Intent(Global.NotificationServiceBroadcastReceiver)
        intent.putExtra(Global.MUTE_STATE_KEY,getMuteState())
        context.sendBroadcast(intent)
    }

}