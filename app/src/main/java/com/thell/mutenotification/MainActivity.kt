package com.thell.mutenotification


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Spanned
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.fragment.NavigationDrawerFragment
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.PermissionHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverPrefHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import com.thell.mutenotification.services.MuteNotificationListenerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity()
{

//-----------------------------------VARIABLES------------------------------------------------------

    private lateinit var MuteStateAction : IMuteStateAction
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dialog:AlertDialog
    private var isPermission = true
    private lateinit var toolbar: Toolbar


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

        override fun onClick(p0: View)
        {
            GuiHelper.startRotatingView(null,p0,::coreClick)
        }

        private fun coreClick()
        {

            if(!::alertDialogBuilder.isInitialized)
            {
                val message =
                    "${HtmlCompat.fromHtml(getString(R.string.info),HtmlCompat.FROM_HTML_MODE_LEGACY)}" +
                            "Version: ${Global.VERSION}"
                alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle(R.string.app_name)
                alertDialogBuilder.setMessage(message)
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

    private val menuOnClick = object :View.OnClickListener
    {

        override fun onClick(p0: View)
        {
            GuiHelper.startRotatingView(null,p0,::coreClick)
        }

        private fun coreClick()
        {
            openDrawerLayout()
        }

    }

    private val closeMenuOnClick = object :View.OnClickListener
    {

        override fun onClick(p0: View)
        {
            GuiHelper.startRotatingView(null,p0,::coreClick)
        }

        private fun coreClick()
        {
            closeDrawerLayout()
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
        GuiHelper.setTextViewPatternBackground(resources,R.drawable.pattern,fragment_navigation_drawer_header_textView)
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

    private fun setupNavigationDrawer()
    {
        val navFrag = supportFragmentManager.findFragmentById(R.id.mainActivityDrawerLayoutFragment) as NavigationDrawerFragment
        navFrag.setupDrawertoogle(mainActivityDrawerLayout,mainActivityToolbar as Toolbar)

    }

    private fun setupToolbar()
    {
        toolbar = mainActivityToolbar as Toolbar
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
        setupNavigationDrawer()
        setupToolbar()
        drawerLayout = mainActivityDrawerLayout
        mainActivityInfoButton.setOnClickListener(infoOnClick)
        mainActivityMenuButton.setOnClickListener(menuOnClick)
        fragment_navigation_drawer_close_button.setOnClickListener(closeMenuOnClick)
    }


    @SuppressLint("WrongConstant")
    fun closeDrawerLayout()
    {
        if(::drawerLayout.isInitialized)
            drawerLayout.post {
                drawerLayout.closeDrawer(Gravity.START, true)
            }

    }

    @SuppressLint("WrongConstant")
    fun openDrawerLayout()
    {
        if(::drawerLayout.isInitialized)
            drawerLayout.post {
                drawerLayout.openDrawer(Gravity.START, true)
            }

    }


    private fun initUI()
    {

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