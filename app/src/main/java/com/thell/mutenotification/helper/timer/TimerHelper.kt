package com.thell.mutenotification.helper.timer

import android.content.Context
import com.thell.mutenotification.database.entity.TimerEntity
import com.thell.mutenotification.helper.database.DatabaseHelper

class TimerHelper private constructor()
{
    companion object
    {
        var CurrentTimer : TimerEntity? = null

        fun loadTimer(context: Context)
        {
            CurrentTimer = DatabaseHelper.getInstance(context).getTimerDao().getLastTimer()
        }

        fun updateTimer(context: Context)
        {
            val id = CurrentTimer!!.ID
            if(CurrentTimer != null)
            {
                Thread{
                    DatabaseHelper.getInstance(context).getTimerDao().update(id,true)
                }.start()

            }
            CurrentTimer = null
        }

        fun insertTimer(context: Context,timerEntity: TimerEntity)
        {
            Thread{
                DatabaseHelper.getInstance(context).getTimerDao().insert(timerEntity)
            }.start()
            CurrentTimer = timerEntity
        }
    }
}