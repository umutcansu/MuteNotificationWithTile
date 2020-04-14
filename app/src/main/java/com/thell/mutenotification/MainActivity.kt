package com.thell.mutenotification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.PermissionHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverPrefHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.services.MuteNotificationListenerService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{

    private lateinit var MuteStateAction : IMuteStateAction
    lateinit var dialog:android.app.AlertDialog
    var isPermission = true

    private val switchChange = object : CompoundButton.OnCheckedChangeListener
    {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean)
        {
            if (::MuteStateAction.isInitialized)
                MuteStateAction.switchMuteState()

            if(p1)
            {
                init()
            }
            else
            {

            }
        }
    }

    private val receiver = object : NotificationServiceBroadcastReceiver()
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
        NotificationServiceHelper.start(this)
    }

    private fun initUI()
    {
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        registerReceiver(receiver, filter)
        mainActivityMuteSwitch.isChecked = Global.getMuteStateAction(this).getMuteState()
        mainActivityMuteSwitch.setOnCheckedChangeListener(switchChange)
        init()
    }


    override fun onDestroy() {

        try
        {
            unregisterReceiver(receiver)

        }
        catch (e: Exception)
        {

        }
        super.onDestroy()

    }

    override fun onResume()
    {
        if(isPermission)
            checkAndRequestNotificationPermission()

        super.onResume()
    }



    override fun onStop() {
        super.onStop()

        if(this::dialog.isInitialized)
            dialog.dismiss()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == Global.NOTIFICATION_PERMISSION_REQUEST_CODE)
        {
            checkAndRequestNotificationPermission()
        }
        else if(requestCode == Global.BOOT_PERMISSION_REQUEST_CODE)
        {
            checkAndRequestBootReceiverPermission()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        NotificationServiceHelper.muteNotificationService = Intent(this, MuteNotificationListenerService::class.java)
        setContentView(R.layout.activity_main)
        MuteStateAction = Global.getMuteStateAction(this)
    }

    fun notificationPermissionDialogListener(state:Boolean)
    {
        if(!state)
        {
            Toast.makeText(this,getString(R.string.permission_request_cancel_message), Toast.LENGTH_LONG).show()
            checkAndRequestNotificationPermission()
        }
    }

    fun checkAndRequestBootReceiverPermission()
    {
        try
        {
            if(!BootReceiverPrefHelper.readBoolean(this))
            {
                isPermission = false
               PermissionHelper.buildBootReceiverAlertDialog(this)
            }
            else
            {
                initUI()
            }
        }
        catch (e: Exception)
        {

        }
    }

    fun checkAndRequestNotificationPermission()
    {
        try
        {
            if(!PermissionHelper.isNotificationServiceEnabled(this))
            {

                dialog = PermissionHelper.buildNotificationServiceAlertDialog(this,::notificationPermissionDialogListener)
                dialog.show()
            }
            else
            {
                checkAndRequestBootReceiverPermission()
            }
        }
        catch (e: Exception)
        {

        }
    }

}
