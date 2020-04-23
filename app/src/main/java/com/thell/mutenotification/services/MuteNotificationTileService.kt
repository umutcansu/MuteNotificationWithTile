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
import com.thell.mutenotification.helper.notificationservice.NotificationServiceHelper
import com.thell.mutenotification.helper.permission.PermissionHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction

class MuteNotificationTileService: TileService()
{

    private lateinit var MuteStateAction : IMuteStateAction

    override fun onCreate() {
        super.onCreate()
        MuteStateAction = Global.getMuteStateAction(this)
    }

    override fun onClick()
    {
        Log.i("tile","onClick")
        super.onClick()
        if(!PermissionHelper.isNotificationServiceEnabled(this))
        {
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivityAndCollapse(intent)
        }
        else
        {

            val state = MuteStateAction.getMuteState()

            if(state)
                NotificationServiceHelper.stopNotificationListenerService(this)
            else
                NotificationServiceHelper.start(this)

            if (::MuteStateAction.isInitialized)
                MuteStateAction.switchMuteState()

            setTile()


        }

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
        super.onStartListening()
        registerReceiver()
        setTile()
    }

    override fun onStopListening() {
        Log.i("tile","onStopListening")
        unregisterReceiver(receiver)
        val tile = qsTile
        tile.apply {
            label =  getString(R.string.loading)
            state = Tile.STATE_UNAVAILABLE
            icon = Icon.createWithResource(this@MuteNotificationTileService, R.drawable.ic_hourglass_full_black_24dp)
            updateTile()
        }
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

        tile.apply {
            if(Global.getMuteStateAction(this@MuteNotificationTileService).getMuteState())
            {
                label = getString(R.string.mute)
                state = Tile.STATE_ACTIVE
                icon = Icon.createWithResource(this@MuteNotificationTileService, R.drawable.ic_notifications_off_black_24dp)

            }
            else
            {
                label =  getString(R.string.notification)
                state = Tile.STATE_INACTIVE
                icon = Icon.createWithResource(this@MuteNotificationTileService, R.drawable.ic_notifications_black_24dp)
            }

            updateTile()
        }


    }



}