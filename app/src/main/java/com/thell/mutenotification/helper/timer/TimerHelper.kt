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

            if(CurrentTimer != null)
            {
                DatabaseHelper.getInstance(context).getTimerDao().update(CurrentTimer!!.ID,true)
            }
            CurrentTimer = null
        }

        fun updateTimerAll(context: Context)
        {

            if(CurrentTimer != null)
            {
                DatabaseHelper.getInstance(context).getTimerDao().updateAll(true)
            }
            CurrentTimer = null
        }

        fun insertTimer(context: Context,timerEntity: TimerEntity)
        {
            DatabaseHelper.getInstance(context).getTimerDao().insert(timerEntity)
            CurrentTimer = timerEntity
        }
    }
}