package com.thell.mutenotification.services

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.thell.mutenotification.MainActivity
import com.thell.mutenotification.R

class MuteNotificationTileService: TileService()
{




    override fun onClick() {
        Log.i("tile","onClick")

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

        super.onStartListening()
    }

    override fun onStopListening() {
        Log.i("tile","onStopListening")
        super.onStopListening()
    }


}