package com.thell.mutenotification.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.thell.mutenotification.helper.Global

class BootBroadcastReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        Global.startApplication(context)
    }

}