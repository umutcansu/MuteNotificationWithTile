package com.thell.mutenotification.helper

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.thell.mutenotification.database.entity.NotificationEntity


class GuiHelper
{
    companion object
    {
        fun setTextViewPatternBackground(resources: Resources,drawable:Int,textview:TextView)
        {
            val bitmap = BitmapFactory.decodeResource(resources,drawable)
            val shader: Shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            textview.paint.shader = shader
        }

        fun getIcon(context: Context, iconId:Int,pack:String):Drawable?
        {
            var result :Drawable? = null
            try
            {
                val manager: PackageManager = context.packageManager
                val resources: Resources = manager.getResourcesForApplication(pack)
                result =  resources.getDrawable(iconId)

            }
            catch (e: PackageManager.NameNotFoundException)
            {
                e.printStackTrace()
            }
            return result
        }

        fun getIcon(context: Context, notificationEntity: NotificationEntity) =
                 getIcon(context,notificationEntity.IconId.toInt(),notificationEntity.PackageName)

    }
}