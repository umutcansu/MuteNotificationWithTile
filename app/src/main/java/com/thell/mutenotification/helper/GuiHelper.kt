package com.thell.mutenotification.helper


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.thell.mutenotification.database.entity.NotificationEntity
import java.util.*


class GuiHelper private constructor()
{


    companion object
    {
        fun setTextViewPatternBackground(resources: Resources,drawable:Int,textview:TextView)
        {
            val bitmap = BitmapFactory.decodeResource(resources,drawable)
            val shader: Shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            textview.paint.shader = shader
        }

        fun getAppNameFromPackageName(context: Context, packageName: String): String
        {
            return try
            {
                val packageManager = context.packageManager
                val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                packageManager.getApplicationLabel(info).toString()
            }
            catch (t:Throwable)
            {
                ""
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun epochToDate(epoch:Long, format:String="yyyy-MM-dd HH:mm"):String
        {
            return try {
                val date = Date(epoch)
                val formatter = java.text.SimpleDateFormat(format)
                return  formatter.format(date)
            }
            catch (t:Throwable)
            {
                ""
            }

        }


        fun getIcon(context: Context,pack:String):Drawable?
        {
            var result :Drawable? = null
            try
            {
                val manager: PackageManager = context.packageManager
                result  = manager.getApplicationIcon(pack)

            }
            catch (e: PackageManager.NameNotFoundException)
            {
                e.printStackTrace()
            }
            return result
        }

        fun getIcon(context: Context, notificationEntity: NotificationEntity) =
                 getIcon(context,notificationEntity.PackageName)

        fun startRotatingView(rotate: Boolean?, view: View, finishedAnimFunc: () -> Unit)
        {

            try {
                val animSet = AnimationSet(true)
                animSet.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) { }

                    override fun onAnimationEnd(animation: Animation) {
                            finishedAnimFunc()
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })

                if(rotate== null)
                {
                    animSet.interpolator = DecelerateInterpolator()
                    animSet.fillAfter = true
                    animSet.isFillEnabled = true

                    val animRotate = RotateAnimation(
                        0.0f, -360.0f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f
                    )

                    animRotate.duration = 500
                    animRotate.fillAfter = true

                    animSet.addAnimation(animRotate)

                    view.startAnimation(animSet)
                    return
                }

                if (rotate)
                {

                    animSet.interpolator = DecelerateInterpolator()
                    animSet.fillAfter = true
                    animSet.isFillEnabled = true

                    val animRotate = RotateAnimation(
                        0.0f, -180.0f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f
                    )

                    animRotate.duration = 500
                    animRotate.fillAfter = true

                    animSet.addAnimation(animRotate)

                    view.startAnimation(animSet)
                }
                else
                {

                    animSet.interpolator = DecelerateInterpolator()
                    animSet.fillAfter = true
                    animSet.isFillEnabled = true

                    val animRotate = RotateAnimation(
                        -180.0f, 0.0f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f
                    )

                    animRotate.duration = 500
                    animRotate.fillAfter = true
                    animSet.addAnimation(animRotate)
                    view.startAnimation(animSet)
                }
            }
            catch (e: Exception)
            {

            }

        }

    }
}