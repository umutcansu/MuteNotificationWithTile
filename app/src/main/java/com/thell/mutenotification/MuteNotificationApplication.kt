package com.thell.mutenotification

import android.app.Application
import android.util.Log
import com.thell.mutenotification.helper.Global

class MuteNotificationApplication : Application()
{
    private lateinit var uncaughtExceptionHandler:  Thread.UncaughtExceptionHandler

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(handler)
        init()
    }

    private var  handler = Thread.UncaughtExceptionHandler { p0, p1 ->
        if(p1.message != null)
            Log.e("UncaughtException",p1.message!!)

        uncaughtExceptionHandler.uncaughtException(p0, p1);
    };

    private fun init()
    {
       Global.startApplication(this)
    }


}

