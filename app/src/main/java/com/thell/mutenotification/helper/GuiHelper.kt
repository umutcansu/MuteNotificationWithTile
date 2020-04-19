package com.thell.mutenotification.helper


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