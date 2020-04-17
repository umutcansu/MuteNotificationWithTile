package com.thell.mutenotification

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Shader
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.PermissionHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverPrefHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.services.MuteNotificationListenerService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{

//-----------------------------------VARIABLES------------------------------------------------------

    private lateinit var MuteStateAction : IMuteStateAction
    lateinit var dialog:android.app.AlertDialog
    var isPermission = true

    private val switchChange = object : CompoundButton.OnCheckedChangeListener
    {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean)
        {
            if (::MuteStateAction.isInitialized)
                MuteStateAction.switchMuteState()
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

    private val infoOnClick = object :View.OnClickListener
    {

        lateinit var dialog:AlertDialog
        lateinit var alertDialogBuilder:AlertDialog.Builder

        override fun onClick(p0: View?)
        {
            if(!::alertDialogBuilder.isInitialized)
            {
                alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle(R.string.app_name)
                alertDialogBuilder.setMessage(HtmlCompat.fromHtml(getString(R.string.info),HtmlCompat.FROM_HTML_MODE_LEGACY))
                alertDialogBuilder.setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            }
            if(!::dialog.isInitialized)
            {
                dialog = alertDialogBuilder.create()
            }

            dialog.show()

        }

    }

//-----------------------------------ANDROID--------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        NotificationServiceHelper.muteNotificationService = Intent(this, MuteNotificationListenerService::class.java)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        GuiHelper.setTextViewPatternBackground(resources,R.drawable.pattern,mainActivityHeaderTextView)
        MuteStateAction = Global.getMuteStateAction(this)
    }

    override fun onResume()
    {
        if(isPermission)
            checkAndRequestNotificationPermission()

        super.onResume()
    }

    override fun onStop()
    {
        super.onStop()

        if(this::dialog.isInitialized)
            dialog.dismiss()
    }

    override fun onDestroy()
    {

        try
        {
            unregisterReceiver(receiver)
        }
        catch (e: Exception)
        {

        }
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

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

//-----------------------------------BUSINESS-------------------------------------------------------
    private fun checkNotificationState(state:Boolean)
    {

        mainActivityMuteSwitch.setOnCheckedChangeListener(null)
        mainActivityMuteSwitch.isChecked = state
        mainActivityMuteSwitch.setOnCheckedChangeListener(switchChange)
        setState(state)
    }

    private  fun setStateInit()
    {
        val state = Global.getMuteStateAction(this).getMuteState()
        checkNotificationState(state)
    }

    private  fun setState(state:Boolean)
    {
        mainActivityMuteStateTextView.apply {
            if(state)
            {
                text = getString(R.string.mute)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorMuteSetState))
            }
            else
            {
                text = getString(R.string.notification)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorNotificationSetState))
            }

        }

        mainActivityMuteStateExpTextView.apply {

            val sp : Spanned = when(state)
            {
                true -> HtmlCompat.fromHtml(getString(R.string.muteexp),HtmlCompat.FROM_HTML_MODE_LEGACY)
                else -> HtmlCompat.fromHtml(getString(R.string.notificationexp),HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            text = sp

        }

    }

    private fun init()
    {
        NotificationServiceHelper.start(this)
    }





    private fun initUI()
    {
        mainActivityInfoButton.setOnClickListener(infoOnClick)
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        registerReceiver(receiver, filter)
        setStateInit()
        mainActivityMuteSwitch.setOnCheckedChangeListener(switchChange)
        init()
    }

    private fun notificationPermissionDialogListener(state:Boolean)
    {
        if(!state)
        {
            Toast.makeText(this,getString(R.string.permission_request_cancel_message), Toast.LENGTH_LONG).show()
            checkAndRequestNotificationPermission()
        }
    }

    private fun systemAlertPermissionDialogListener(state:Boolean)
    {
        if(!state)
        {
            Toast.makeText(this,getString(R.string.permission_request_cancel_message), Toast.LENGTH_LONG).show()
            checkAndRequestSystemAlertPermission()
        }
    }

    private fun checkAndRequestBootReceiverPermission()
    {
        try
        {

            if(BootReceiverHelper.ınstance.checkPrePermission())
            {
                if(!BootReceiverPrefHelper.readBoolean(this)  )
                {
                    isPermission = false
                    PermissionHelper.buildBootReceiverAlertDialog(this)
                }
                else
                {
                    initUI()
                }
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

    private fun checkAndRequestSystemAlertPermission()
    {
        try
        {

            if(PermissionHelper.checkSystemAlertPermission(this))
            {
                val dialog = PermissionHelper.buildSystemAlertPermissionAlertDialog(this,::systemAlertPermissionDialogListener)
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

    private fun checkAndRequestNotificationPermission()
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