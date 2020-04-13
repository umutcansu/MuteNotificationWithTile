package com.thell.mutenotification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.thell.mutenotification.broadcastreciever.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.Global.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val switchChange = object : CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean)
        {

        }
    }

    val reciever = object : NotificationServiceBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            super.onReceive(p0, p1)
            val state = p1!!.getBooleanExtra(Global.MUTE_STATE_KEY,false)
            checkNotificationState(state)
        }
    }

    private fun init()
    {

    }

    private fun initUI()
    {
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        registerReceiver(reciever, filter)

        mainActivityMuteSwitch.setOnCheckedChangeListener(switchChange)
    }



    override fun onPause() {
        super.onPause()
        unregisterReceiver(reciever)
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }



    private fun checkNotificationState(state:Boolean)
    {

        if(state != mainActivityMuteSwitch.isChecked)
        {
            mainActivityMuteSwitch.setOnCheckedChangeListener(null)
            mainActivityMuteSwitch.isChecked = state
            mainActivityMuteSwitch.setOnCheckedChangeListener(switchChange)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(intent.getBooleanExtra(Global.PERMISSION_STATE_KEY,false))
        {
            initUI()
            init()
        }

    }

}
