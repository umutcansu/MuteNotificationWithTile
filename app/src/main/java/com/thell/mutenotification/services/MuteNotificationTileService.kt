package com.thell.mutenotification.services

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.thell.mutenotification.MainActivity
import com.thell.mutenotification.R
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.mutestate.IMuteStateAction

class MuteNotificationTileService: TileService()
{

    private lateinit var MuteStateAction : IMuteStateAction

    override fun onCreate() {
        super.onCreate()
        MuteStateAction = Global.getMuteStateAction(this)
    }

    override fun onClick() {
        Log.i("tile","onClick")

        if (::MuteStateAction.isInitialized)
            MuteStateAction.switchMuteState()

        super.onClick()

    }

    override fun onTileRemoved() {

        super.onTileRemoved()
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onTileAdded() {

        super.onTileAdded()
    }

    override fun onBind(intent: Intent?): IBinder? {

        return super.onBind(intent)
    }


    override fun onStartListening() {
        Log.i("tile","onStartListening")
        registerReceiver()
        setTile()
        super.onStartListening()
    }

    override fun onStopListening() {
        Log.i("tile","onStopListening")
        unregisterReceiver(receiver)
        super.onStopListening()
    }

    private val receiver = object : NotificationServiceBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            super.onReceive(p0, p1)
            setTile()
        }
    }

    private fun registerReceiver()
    {
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        registerReceiver(receiver, filter)
    }

    fun setTile()
    {
        val tile = qsTile
        if(Global.getMuteStateAction(this).getMuteState())
        {
            tile.label = "Mute"
            tile.state = Tile.STATE_ACTIVE
            tile.icon = Icon.createWithResource(this, R.drawable.ic_notifications_off_black_24dp)

        }
        else
        {
            tile.label = "Notification"
            tile.state = Tile.STATE_INACTIVE
            tile.icon = Icon.createWithResource(this, R.drawable.ic_notifications_black_24dp)
        }
        tile.state = tile.state
        tile.updateTile()
    }



}