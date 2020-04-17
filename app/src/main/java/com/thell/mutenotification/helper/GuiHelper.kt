package com.thell.mutenotification.helper

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Shader
import android.widget.TextView

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
    }
}