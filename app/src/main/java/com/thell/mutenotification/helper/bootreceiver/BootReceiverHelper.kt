package com.thell.mutenotification.helper.bootreceiver

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.thell.mutenotification.helper.Global


class BootReceiverHelper private constructor() {
    /***
     * Xiaomi
     */
    private val BRAND_XIAOMI = "xiaomi"
    private val PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter"
    private val PACKAGE_XIAOMI_COMPONENT =
        "com.miui.permcenter.autostart.AutoStartManagementActivity"

    /***
     * Letv
     */
    private val BRAND_LETV = "letv"
    private val PACKAGE_LETV_MAIN = "com.letv.android.letvsafe"
    private val PACKAGE_LETV_COMPONENT =
        "com.letv.android.letvsafe.AutobootManageActivity"

    /***
     * ASUS ROG
     */
    private val BRAND_ASUS = "asus"
    private val PACKAGE_ASUS_MAIN = "com.asus.mobilemanager"
    private val PACKAGE_ASUS_COMPONENT =
        "com.asus.mobilemanager.powersaver.PowerSaverSettings"

    /***
     * Honor
     */
    private val BRAND_HONOR = "honor"
    private val PACKAGE_HONOR_MAIN = "com.huawei.systemmanager"
    private val PACKAGE_HONOR_COMPONENT =
        "com.huawei.systemmanager.optimize.process.ProtectActivity"

    /**
     * Oppo
     */
    private val BRAND_OPPO = "oppo"
    private val PACKAGE_OPPO_MAIN = "com.coloros.safecenter"
    private val PACKAGE_OPPO_FALLBACK = "com.oppo.safe"
    private val PACKAGE_OPPO_COMPONENT =
        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK =
        "com.oppo.safe.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK_A =
        "com.coloros.safecenter.startupapp.StartupAppListActivity"

    /**
     * Vivo
     */
    private val BRAND_VIVO = "vivo"
    private val PACKAGE_VIVO_MAIN = "com.iqoo.secure"
    private val PACKAGE_VIVO_FALLBACK = "com.vivo.perm;issionmanager"
    private val PACKAGE_VIVO_COMPONENT =
        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK =
        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK_A =
        "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"

    /**
     * Nokia
     */
    private val BRAND_NOKIA = "nokia"
    private val PACKAGE_NOKIA_MAIN = "com.evenwell.powersaving.g3"
    private val PACKAGE_NOKIA_COMPONENT =
        "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"

    fun getAutoStartPermission(activity: Activity) {
        if (BootReceiverPrefHelper.readBoolean(activity))
            return

        val build_info = Build.BRAND.toLowerCase()
        when (build_info) {
            BRAND_ASUS -> autoStartAsus(activity)
            BRAND_XIAOMI -> autoStartXiaomi(activity)
            BRAND_LETV -> autoStartLetv(activity)
            BRAND_HONOR -> autoStartHonor(activity)
            BRAND_OPPO -> autoStartOppo(activity)
            BRAND_VIVO -> autoStartVivo(activity)
            BRAND_NOKIA -> autoStartNokia(activity)
        }
    }

    fun checkPrePermission():Boolean
    {
        val build_info = Build.BRAND.toLowerCase()
        var result = when (build_info) {
            BRAND_ASUS -> true
            BRAND_XIAOMI -> true
            BRAND_LETV -> true
            BRAND_HONOR -> true
            BRAND_OPPO -> true
            BRAND_VIVO -> true
            BRAND_NOKIA -> true
            else -> false
        }
        return result
    }

    private fun autoStartAsus(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_ASUS_MAIN)) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_ASUS_MAIN, PACKAGE_ASUS_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun showAlert(
        context: Context,
        onClickListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context).setTitle("Allow AutoStart")
            .setMessage("Please enable auto start in settings.")
            .setPositiveButton("Allow", onClickListener).show().setCancelable(false)
    }

    private fun autoStartXiaomi(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_XIAOMI_MAIN)) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.writeBoolean(activity, true)
                    startIntent(
                        activity,
                        PACKAGE_XIAOMI_MAIN,
                        PACKAGE_XIAOMI_COMPONENT
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    private fun autoStartLetv(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_LETV_MAIN)) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_LETV_MAIN, PACKAGE_LETV_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    private fun autoStartHonor(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_HONOR_MAIN)) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_HONOR_MAIN, PACKAGE_HONOR_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    private fun autoStartOppo(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_OPPO_MAIN) || isPackageExists(
                activity,
                PACKAGE_OPPO_FALLBACK
            )
        ) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                        startIntent(
                            activity,
                            PACKAGE_OPPO_FALLBACK,
                            PACKAGE_OPPO_COMPONENT_FALLBACK
                        )
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        try {
                            BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                            startIntent(
                                activity,
                                PACKAGE_OPPO_MAIN,
                                PACKAGE_OPPO_COMPONENT_FALLBACK_A
                            )
                        } catch (exx: Exception) {
                            exx.printStackTrace()
                        }
                    }
                }
            })
        }
    }

    private fun autoStartVivo(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_VIVO_MAIN) || isPackageExists(
                activity,
                PACKAGE_VIVO_FALLBACK
            )
        ) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                        startIntent(
                            activity,
                            PACKAGE_VIVO_FALLBACK,
                            PACKAGE_VIVO_COMPONENT_FALLBACK
                        )
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        try {
                            BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                            startIntent(
                                activity,
                                PACKAGE_VIVO_MAIN,
                                PACKAGE_VIVO_COMPONENT_FALLBACK_A
                            )
                        } catch (exx: Exception) {
                            exx.printStackTrace()
                        }
                    }
                }
            })
        }
    }

    private fun autoStartNokia(activity: Activity) {
        if (isPackageExists(activity, PACKAGE_NOKIA_MAIN)) {
            showAlert(activity, DialogInterface.OnClickListener { dialog, which ->
                try {
                    BootReceiverPrefHelper.Companion.writeBoolean(activity, true)
                    startIntent(activity, PACKAGE_NOKIA_MAIN, PACKAGE_NOKIA_COMPONENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    private fun startIntent(
        activity: Activity,
        packageName: String,
        componentName: String
    ) {
        try {
            val intent = Intent()
            intent.component = ComponentName(packageName, componentName)
            activity.startActivityForResult(intent,Global.BOOT_PERMISSION_REQUEST_CODE)
        } catch (var5: Exception) {
            var5.printStackTrace()
            throw var5
        }
    }

    private fun isPackageExists(
        context: Context,
        targetPackage: String
    ): Boolean {
        val packages: List<ApplicationInfo>
        val pm = context.packageManager
        packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName == targetPackage) {
                return true
            }
        }
        return false
    }

    companion object {
        val ınstance: BootReceiverHelper
            get() = BootReceiverHelper()
    }
}