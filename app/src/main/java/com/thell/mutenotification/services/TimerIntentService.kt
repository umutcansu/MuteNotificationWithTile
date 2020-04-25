package com.thell.mutenotification.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.thell.mutenotification.broadcastreceiver.TimerBroadcastReceiver
import com.thell.mutenotification.database.entity.TimerEntity
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.timer.TimerHelper
import java.util.*


class TimerIntentService : IntentService("TimerIntentService")
{

    val period = 1000L
    val delay = 0L
    var IS_RUNNIG = true
    lateinit var timer : Timer
    private lateinit var timerTask :TimerTask
    var handler = Handler(Looper.getMainLooper())

    private fun createdTimer(context: Context,intent: Intent)
    {
        val timer = intent.getSerializableExtra(TimerHelper.TimerEntity) as TimerEntity
        TimerHelper.insertTimer(context,timer)
        startTimer()
        setTimerSendBroadcast()
    }

    private fun canceledTimer()
    {
        stopTimer()
        finishedTimerSendBroadcast()
    }

    private val receiver = object : TimerBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            if(p1 != null)
            {
                val isCreated = p1.getBooleanExtra(TimerHelper.TimerCreatedFlag,false)
                if(isCreated)
                {
                   createdTimer(p0!!,p1)
                }

                val isCanceled = p1.getBooleanExtra(TimerHelper.TimerCanceledFlag,false)
                if(isCanceled)
                {
                   canceledTimer()
                }
            }
            super.onReceive(p0, p1)
        }
    }

    private fun startTimer()
    {
        if(TimerHelper.CurrentTimer != null)
        {
            timer = Timer()
            timerTask = object :TimerTask()
            {
                override fun run()
                {
                    checkTimerState()
                }
            }
            timer.scheduleAtFixedRate(timerTask, delay, period)
        }
    }

    private fun finishedTimerSendBroadcast()
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)
        intent.putExtra(TimerHelper.TimerFinishedFlag,true)
        sendBroadcast(intent)
    }

    private fun setTimerSendBroadcast()
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)
        intent.putExtra(TimerHelper.TimerSetFlag,true)
        sendBroadcast(intent)
    }

    private fun stopTimer()
    {
        TimerHelper.updateTimerAll(this)
        timer.cancel()
        timerTask.cancel()
    }

    private fun registerReceiver()
    {
        val filter = IntentFilter(Global.TimerBroadcastReceiver)
        registerReceiver(receiver, filter)
    }


    override fun onHandleIntent(intent: Intent?)
    {
        Log.e("TimerIntentService","onHandleIntent")
        registerReceiver()
        while (IS_RUNNIG)
        {

        }
    }

    private fun checkTimerState()
    {
        if(TimerHelper.CurrentTimer != null)
        {
            val currentDate = System.currentTimeMillis()
            val millisecond = TimerHelper.CurrentTimer!!.TimerTime - currentDate

            if(millisecond <= 0)
            {
                doProcess()
                canceledTimer()
            }
        }
    }

    private fun doProcess()
    {
        handler.post {
            Toast.makeText(this,"Timer Bitti.",Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroy()
    {
        super.onDestroy()
        unregisterReceiver(receiver)
        Log.e("TimerIntentService","onDestroy")

    }

}
