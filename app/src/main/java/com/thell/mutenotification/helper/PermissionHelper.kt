package com.thell.mutenotification.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.thell.mutenotification.R
import com.thell.mutenotification.helper.bootreceiver.BootReceiverHelper

class PermissionHelper {

    companion object
    {
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
            alertDialogBuilder.apply {
                setTitle(R.string.notification_listener_service)
                setMessage(R.string.notification_listener_service_explanation)
                setCancelable(false)
                setPositiveButton(
                    R.string.yes,
                    DialogInterface.OnClickListener { _, _ ->
                        listener(true)
                        activity.startActivityForResult(
                            Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS),Global.NOTIFICATION_PERMISSION_REQUEST_CODE
                        )
                    })
                setNegativeButton(
                    R.string.no,
                    DialogInterface.OnClickListener { dialog, _ ->
                        listener(false)
                        dialog.dismiss()
                    })
            }

            return alertDialogBuilder.create()
        }

        fun buildSystemAlertPermissionAlertDialog(activity: Activity,listener:(Boolean)->Unit): AlertDialog
        {
            val alertDialogBuilder = AlertDialog.Builder(activity)
            alertDialogBuilder.apply {
                setTitle(R.string.notification_listener_service)
                setMessage(R.string.system_alert_permission_explanation)
                setCancelable(false)
                setPositiveButton(
                    R.string.yes,
                    DialogInterface.OnClickListener { _, _ ->
                        listener(true)
                       requestSystemAlertPermission(activity)
                    })
                setNegativeButton(
                    R.string.no,
                    DialogInterface.OnClickListener { dialog, _ ->
                        listener(false)
                        dialog.dismiss()
                    })
            }

            return alertDialogBuilder.create()
        }

       fun  checkSystemAlertPermission(activity:Activity):Boolean
       {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!Settings.canDrawOverlays(activity))
                {
                    return true
                }
            }
           return false
        }

        private fun requestSystemAlertPermission(activity: Activity)
        {

            //https://stackoverflow.com/questions/59214359/anyone-knows-where-is-start-in-background-permission-in-miui-11/59380516#59380516
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.packageName)
            )
            activity.startActivityForResult(intent, Global.SYSTEM_ALERT_REQUEST_CODE)

        }

    }
}