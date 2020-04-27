package com.thell.mutenotification.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.thell.mutenotification.R
import com.thell.mutenotification.broadcastreceiver.TimerBroadcastReceiver
import com.thell.mutenotification.database.entity.TimerEntity
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.mutestate.MuteStateActionHelper
import com.thell.mutenotification.helper.timer.TimerHelper
import java.util.*


class TimerIntentService : IntentService("TimerIntentService")
{

    var period :Long = 0
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
        var state = !TimerHelper.CurrentTimer!!.State
        stopTimer()
        finishedTimerSendBroadcast()
        MuteStateActionHelper.getMuteStateAction(this).setMuteState(state)
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
                   MuteStateActionHelper.getMuteStateAction(p0).setMuteState(TimerHelper.CurrentTimer!!.State)
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
        else
        {
            IS_RUNNIG = false
        }
    }

    private fun finishedTimerSendBroadcast()
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)
        intent.putExtra(TimerHelper.TimerFinishedFlag,true)
        sendBroadcast(intent)
    }

    private fun listenerTimerSendBroadcast()
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)
        intent.putExtra(TimerHelper.ListenerTimerFlag,true)
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
        if(::timer.isInitialized)
            timer.cancel()
        if(::timerTask.isInitialized)
            timerTask.cancel()
    }

    private fun registerReceiver()
    {
        listenerTimerSendBroadcast()
        val filter = IntentFilter(Global.TimerBroadcastReceiver)
        registerReceiver(receiver, filter)
    }


    override fun onHandleIntent(intent: Intent?)
    {
        Log.e("TimerIntentService","onHandleIntent")
        period =  1000*Global.PERIOD
        registerReceiver()
        TimerHelper.SERVICE_IS_RUNNIG = true
        while (IS_RUNNIG)
        {

        }
        onDestroy()

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
        else
        {
            IS_RUNNIG = false
        }
    }

    private fun doProcess()
    {
        handler.post {
            Toast.makeText(this,getString(R.string.finishTimer),Toast.LENGTH_LONG).show()
        }

    }


    override fun onDestroy()
    {
        super.onDestroy()
        TimerHelper.SERVICE_IS_RUNNIG = false
        try
        {
            unregisterReceiver(receiver)
        }
        catch (e: Exception)
        {
        }
        Log.e("TimerIntentService","onDestroy")

    }

}
