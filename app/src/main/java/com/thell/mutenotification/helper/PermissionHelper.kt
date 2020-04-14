package com.thell.mutenotification.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import com.thell.mutenotification.R
import com.thell.mutenotification.helper.bootreceiver.BootReceiverHelper

class PermissionHelper {

    companion object
    {



        var muteNotificationService:Intent? = null
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private const  val  ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"



        fun isNotificationServiceEnabled(context: Context): Boolean {

            val flat = Settings.Secure.getString(context.contentResolver, ENABLED_NOTIFICATION_LISTENERS)
            if (flat.isNotEmpty()) {
                val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in names.indices)
                {
                    val cn = ComponentName.unflattenFromString(names[i])
                    if (cn != null)
                    {
                        if (context.packageName == cn.packageName) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun buildBootReceiverAlertDialog(activity: Activity)
        {
            BootReceiverHelper.ınstance.getAutoStartPermission(activity)
        }


        fun buildNotificationServiceAlertDialog(activity: Activity,listener:(Boolean)->Unit): AlertDialog
        {
            val alertDialogBuilder = AlertDialog.Builder(activity)
            alertDialogBuilder.setTitle(R.string.notification_listener_service)
            alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
            alertDialogBuilder.setPositiveButton(
                R.string.yes,
                DialogInterface.OnClickListener { dialog, id ->
                    listener(true)
                    activity.startActivityForResult(
                        Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS),Global.NOTIFICATION_PERMISSION_REQUEST_CODE
                    )
                })
            alertDialogBuilder.setNegativeButton(
                R.string.no,
                DialogInterface.OnClickListener { dialog, id ->
                    listener(false)
                    dialog.dismiss()
                })
            return alertDialogBuilder.create()
        }


    }
}