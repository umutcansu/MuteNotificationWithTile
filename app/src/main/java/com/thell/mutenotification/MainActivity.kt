package com.thell.mutenotification


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.thell.mutenotification.fragment.HistoryFragment
import com.thell.mutenotification.fragment.MainFragment
import com.thell.mutenotification.fragment.NavigationDrawerFragment
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.PermissionHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverHelper
import com.thell.mutenotification.helper.bootreceiver.BootReceiverPrefHelper
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import com.thell.mutenotification.model.NavigationDrawerItem
import com.thell.mutenotification.services.MuteNotificationListenerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity()
{

//-----------------------------------VARIABLES------------------------------------------------------

    private lateinit var navigationHeaderTextView:TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var  navFrag :NavigationDrawerFragment
    private lateinit var manager: FragmentManager
    private lateinit var dialog:AlertDialog
    private var isPermission = true
    private lateinit var toolbar: Toolbar
    private var backPressedTime = 0L
    private var timeOut = 1000



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
        NotificationServiceHelper.muteNotificationService =
            Intent(this, MuteNotificationListenerService::class.java)
        setupFullScreen()
        setContentView(R.layout.activity_main)
        initUI()
        init()
    }


    override fun onResume()
    {

        if(isPermission)
            checkAndRequestNotificationPermission()

        super.onResume()
    }



    override fun onStop()
    {
        Log.e("mainActivity","onStop")
        super.onStop()

        if(this::dialog.isInitialized)
            dialog.dismiss()
    }

    override fun onDestroy()
    {
        try
        {

        }
        catch (e: Exception)
        {

        }
        super.onDestroy()

    }

//-------------------------------------------NAVIGATION---------------------------------------------
    private fun setupNavigationDrawer()
    {
        navFrag = supportFragmentManager.findFragmentById(R.id.mainActivityDrawerLayoutFragment) as NavigationDrawerFragment
        navFrag.setupDrawerToggle(mainActivityDrawerLayout,mainActivityToolbar as Toolbar,::menuChangeListener)
        GuiHelper.setTextViewPatternBackground(resources,R.drawable.pattern,fragment_navigation_drawer_header_textView)
    }

    private fun menuChangeListener(menu:NavigationDrawerItem)
    {
        when(menu.title)
        {
            NavigationDrawerItem.HOME -> mainFragmentOpen()
            NavigationDrawerItem.HISTORY -> notificationHistoryFragmentOpen()
        }
        closeDrawerLayout()
    }

    private val fragmentCommunication = object :IFragmentCommunication
    {
        override fun changeHeader(header: String)
        {
            navigationHeaderTextView.visibility = View.VISIBLE
            navigationHeaderTextView.text = header
            if(header.isNotEmpty())
            {
                for (menu in NavigationDrawerItem.allMenuItem)
                {
                    menu.selected = menu.title == header
                }
            }

            navFrag.setupRecyclerView(this@MainActivity::menuChangeListener)

        }
    }

    private fun mainFragmentOpen()
    {
        changeFragment(MainFragment(fragmentCommunication))
    }

    private fun notificationHistoryFragmentOpen()
    {
        changeFragment(HistoryFragment(fragmentCommunication))

    }

    private fun setupToolbar()
    {
        toolbar = mainActivityToolbar as Toolbar
    }

    private fun changeFragment(fragment: Fragment)
    {

        var transaction = manager.beginTransaction()
        transaction.replace(R.id.mainActivityFragmentContainer, fragment)
        transaction.addToBackStack("addFrag")
        transaction.commit()
        Runtime.getRuntime().gc()
    }

    override fun onBackPressed()
    {

        if (backPressedTime + timeOut > System.currentTimeMillis())
        {
            finish()
            return
        }
        else
        {
            if(manager.backStackEntryCount > 1)
                manager.popBackStack()
            else
                Toast.makeText(this, R.string.quitAppContent, Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()

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

//-----------------------------------BUSINESS-------------------------------------------------------

    private fun setupFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun initToolbarAndDrawerLayout()
    {
        drawerLayout = mainActivityDrawerLayout
        setupNavigationDrawer()
        setupToolbar()
    }

    private fun initButtonClick()
    {
        toolbar_InfoButton.setOnClickListener(infoOnClick)
        toolbar_MenuButton.setOnClickListener(menuOnClick)
        fragment_navigation_drawer_close_button.setOnClickListener(closeMenuOnClick)
    }

    private fun initUI()
    {
        manager = supportFragmentManager
        navigationHeaderTextView = toolbar_navigationHeaderTextView
        initToolbarAndDrawerLayout()
        initButtonClick()
    }

    private fun init()
    {
        mainFragmentOpen()
    }

    private fun start()
    {
        NotificationServiceHelper.start(this)
    }

    private fun stop()
    {

    }




//-------------------------------PERMISSION---------------------------------------------------------

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

    private fun checkAndRequestBootReceiverPermission()
    {
        try
        {

            if(BootReceiverHelper.ınstance.checkPrePermission())
            {
                if(!BootReceiverPrefHelper.readBoolean(this) )
                {
                    isPermission = false
                    PermissionHelper.buildBootReceiverAlertDialog(this)
                }
                else
                {
                    start()
                }
            }
            else
            {
                start()
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
                //init()
            }

        }
        catch (e: Exception)
        {

        }
    }


//--------------------------------------------------------------------------------------------------
}