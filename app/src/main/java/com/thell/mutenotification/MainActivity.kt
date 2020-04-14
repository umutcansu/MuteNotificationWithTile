package com.thell.mutenotification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AlertDialogLayout
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.PermissionHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.services.MuteNotificationListenerService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{

    private lateinit var MuteStateAction : IMuteStateAction
    lateinit var dialog:android.app.AlertDialog

    private val switchChange = object : CompoundButton.OnCheckedChangeListener{
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




    override fun onPause() {
        super.onPause()

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
        checkAndRequestPermission()
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

        if(requestCode == Global.PERMISSION_REQUEST_CODE)
        {
            checkAndRequestPermission()
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

    fun permissionDialogListener(state:Boolean)
    {
        if(!state)
        {
            Toast.makeText(this,getString(R.string.permission_request_cancel_message), Toast.LENGTH_LONG).show()
            checkAndRequestPermission()
        }
    }

    fun checkAndRequestPermission()
    {
        try
        {
            if(!PermissionHelper.isNotificationServiceEnabled(this))
            {

                dialog = PermissionHelper.buildNotificationServiceAlertDialog(this,::permissionDialogListener)
                dialog.show()
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

}
