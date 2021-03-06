package com.thell.mutenotification.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.thell.mutenotification.R
import com.thell.mutenotification.broadcastreceiver.TimerBroadcastReceiver
import com.thell.mutenotification.database.entity.TimerEntity
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.navigation.NavigationMenuHelper
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import com.thell.mutenotification.helper.mutestate.MuteStateActionHelper
import com.thell.mutenotification.helper.timer.TimerHelper
import kotlinx.android.synthetic.main.fragment_timer.view.*
import java.util.*
import java.util.concurrent.TimeUnit


class TimerFragment(private val callback: IFragmentCommunication) : Fragment()
{
//-------------------------------------------VARIABLE-----------------------------------------------

    private val formatter = NumberPicker.Formatter { value -> String.format("%02d", value) }

    private val formatterSecond =  NumberPicker.Formatter() { value -> String.format("%02d", value*Global.PERIOD) }


    private lateinit var hourNumberPicker:NumberPicker
    private lateinit var minuteNumberPicker:NumberPicker
    private lateinit var secondNumberPicker:NumberPicker

    private lateinit var timerFragmentMuteSwitch : ToggleButton

    private lateinit var timerFragmentMuteStateTextView : TextView
    private lateinit var timerFragmentMuteStateExpTextView : TextView

    private lateinit var timerFragmentStartTimerButton : ImageView
    private lateinit var timerFragmentStopTimerButton : ImageView

    private lateinit var timerFragmentNumberPickerLinearLayout: LinearLayout
    private lateinit var timerFragmentTimerCountDownLinearLayout: LinearLayout

    private lateinit var timerFragmentHourTextView : TextView
    private lateinit var timerFragmentMinuteTextView : TextView
    private lateinit var timerFragmentSecondTextView : TextView

    private var isValid  = false
    private lateinit var timer:CountDownTimer

    private val receiver = object : TimerBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {

            if(p1 != null)
            {

                val isListening = p1.getBooleanExtra(TimerHelper.ListenerTimerFlag,false)
                if(isListening)
                {
                    saveTimer()
                }

                val isFinishedID = p1.getBooleanExtra(TimerHelper.TimerFinishedFlag,false)
                if(isFinishedID)
                {
                    clearTimer()
                    controlSwitchEnable(true)

                }

                val isSetTimer = p1.getBooleanExtra(TimerHelper.TimerSetFlag,false)
                if(isSetTimer)
                {
                    setTimer()
                    controlSwitchEnable(false)
                }
            }

            super.onReceive(p0, p1)
        }
    }

//------------------------------------LISTENER------------------------------------------------------

    private val startTimerOnClick = object :View.OnClickListener
    {

        override fun onClick(p0: View)
        {

            if(!TimerHelper.SERVICE_IS_RUNNIG)
            {
                Global.startTimerService(p0.context)
            }
            else
            {
                saveTimerAndStart()
            }
        }

    }

    private val stopTimerOnClick = object :View.OnClickListener
    {

        override fun onClick(p0: View)
        {
            canceledTimerSendBroadcast()
        }

    }


    private val switchChange = object : CompoundButton.OnCheckedChangeListener
    {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean)
        {
           setState(p1)
        }
    }

    private val numberPickerValueChange = object :NumberPicker.OnValueChangeListener
    {
        override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int)
        {
            setButtonState()
        }
    }

//--------------------------------------UI----------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedIS: Bundle?): View
    {

        var view = inflater.inflate(R.layout.fragment_timer, container, false)
        initUI(view)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(context != null)
            context!!.unregisterReceiver(receiver)
        if(::timer.isInitialized)
            timer.cancel()
    }

    override fun onResume() {
        super.onResume()
        init()
    }


    private  fun initUI(view:View)
    {
        callback.changeHeader(NavigationMenuHelper.TIMER)

        hourNumberPicker = view.fragment_timer_hour_numberPicker
        minuteNumberPicker = view.fragment_timer_minute_numberPicker
        secondNumberPicker = view.fragment_timer_second_numberPicker

        hourNumberPicker.setOnValueChangedListener(numberPickerValueChange)
        minuteNumberPicker.setOnValueChangedListener(numberPickerValueChange)
        secondNumberPicker.setOnValueChangedListener(numberPickerValueChange)

        timerFragmentMuteSwitch = view.fragment_timer_MuteSwitch
        timerFragmentMuteSwitch.setOnCheckedChangeListener(switchChange)
        timerFragmentMuteStateExpTextView = view.fragment_timer_MuteStateExpTextView

        timerFragmentMuteStateTextView = view.fragment_timer_MuteStateTextView
        timerFragmentStartTimerButton = view.fragment_timer_StartTimer_Button
        timerFragmentStopTimerButton = view.fragment_timer_StopTimer_Button

        timerFragmentStartTimerButton.setOnClickListener(startTimerOnClick)
        timerFragmentStopTimerButton.setOnClickListener(stopTimerOnClick)

        timerFragmentNumberPickerLinearLayout = view.linearLayoutNumberPicker
        timerFragmentTimerCountDownLinearLayout = view.linearLayoutTimerCountDown

        timerFragmentHourTextView = view.fragment_timer_Hour_TextView
        timerFragmentMinuteTextView = view.fragment_timer_Minute_TextView
        timerFragmentSecondTextView = view.fragment_timer_Second_TextView

    }

    private  fun init()
    {
        registerReceiver()

        setMaxMinNumberPickerValue()
        setStateInit()
        setButtonState()

        timerState()
    }

//----------------------------------------BUSSINES--------------------------------------------------

    private fun timerState()
    {
        if(TimerHelper.CurrentTimer == null)
        {
            visibleNumberPicker()
        }
        else
        {
            visibleCountDown()
            setTimer()
            controlSwitchEnable(false)
        }
    }

    private fun saveTimer()
    {
        if(!isValid)
            return

       val currentDate = System.currentTimeMillis()

       val timer = TimerEntity(
           0,
           currentDate,
           currentDate + calculateTime(),
           timerFragmentMuteSwitch.isChecked
       )
        TimerHelper.insertTimer(context!!,timer)
    }

    private fun saveTimerAndStart()
    {
        if(!isValid)
            return

        val currentDate = System.currentTimeMillis()

        val timer = TimerEntity(
            0,
            currentDate,
            currentDate + calculateTime(),
            timerFragmentMuteSwitch.isChecked
        )

        createTimerSendBroadcast(timer)
    }

    private fun setTimer()
    {
        val currentDate = System.currentTimeMillis()
        val millisecond = TimerHelper.CurrentTimer!!.TimerTime - currentDate

        if(millisecond > 0)
        {
            timer = object : CountDownTimer(millisecond,1_000)
            {
                override fun onFinish()
                {
                    var state = !TimerHelper.CurrentTimer!!.State
                    MuteStateActionHelper.getMuteStateAction(this@TimerFragment.context!!).setMuteState(state)
                    TimerHelper.updateTimerAll(this@TimerFragment.context!!)
                    clearTimer()
                    controlSwitchEnable(true)

                }

                override fun onTick(p0: Long)
                {
                    writeCountDownValue(p0)
                }

            }

            timer.start()
            visibleCountDown()
            visibleStopTimerButton()
        }
    }

    private fun clearTimer()
    {
        stopTimer()

        setMaxMinNumberPickerValue()
        setStateInit()
        setButtonState()


        if(TimerHelper.CurrentTimer == null)
        {
            visibleNumberPicker()
        }
        else
        {
            visibleCountDown()
        }
    }

    private fun stopTimer()
    {
        if(::timer.isInitialized)
            timer.cancel()

        visibleStartTimerButton()
        visibleNumberPicker()
        setMaxMinNumberPickerValue()
    }



    private fun writeCountDownValue(value:Long)
    {
        var value:Long = value

        val days = TimeUnit.MILLISECONDS.toDays(value)
        value -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(value)
        value -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(value)
        value -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(value)

        timerFragmentHourTextView.text =  String.format("%02d", hours)
        timerFragmentMinuteTextView.text = String.format("%02d", minutes)
        timerFragmentSecondTextView.text = String.format("%02d", seconds)
    }

    private fun calculateTime():Long
    {


        var totalSecond: Long

        val hour =   hourNumberPicker.value
        val minute = minuteNumberPicker.value
        val second = secondNumberPicker.value * Global.PERIOD

        totalSecond = (((hour*60)*60) + (minute*60) + second).toLong()

        val milisecond = totalSecond * 1_000

        return milisecond
    }

    private fun setButtonState()
    {
        if(context == null)
            return
        if(hourNumberPicker.value == 0 && minuteNumberPicker.value == 0 && secondNumberPicker.value == 0)
        {
            isValid = false
            timerFragmentStartTimerButton.setColorFilter(ContextCompat.getColor(context!!, R.color.colorNotificationSetState))
        }
        else
        {
            isValid = true
            timerFragmentStartTimerButton.setColorFilter(ContextCompat.getColor(context!!, R.color.colorMuteSetState))
        }
    }

    private  fun setState(state:Boolean)
    {
        if(context == null)
            return

        timerFragmentMuteStateTextView.apply {
            if(state)
            {
                text = getString(R.string.mute)
                setTextColor(ContextCompat.getColor(context, R.color.colorMuteSetState))
            }
            else
            {
                text = getString(R.string.notification)
                setTextColor(ContextCompat.getColor(context, R.color.colorNotificationSetState))
            }

        }

        timerFragmentMuteStateExpTextView.apply {

            val sp : Spanned = when(state)
            {
                true -> HtmlCompat.fromHtml(getString(R.string.muteExp), HtmlCompat.FROM_HTML_MODE_LEGACY)
                else -> HtmlCompat.fromHtml(getString(R.string.notificationExp), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            text = sp

        }

    }

    private  fun setStateInit()
    {
        if(context == null)
            return
        var state = false

        if(TimerHelper.CurrentTimer == null)
            state = !MuteStateActionHelper.getMuteStateAction(context!!).getMuteState()
        else
           state =TimerHelper.CurrentTimer!!.State

        timerFragmentMuteSwitch.setOnCheckedChangeListener(null)
        timerFragmentMuteSwitch.isChecked = state
        timerFragmentMuteSwitch.setOnCheckedChangeListener(switchChange)
        setState(state)
    }

    private fun setMaxMinNumberPickerValue()
    {
        hourNumberPicker.setFormatter(formatter)
        hourNumberPicker.minValue = 0
        hourNumberPicker.maxValue = 23
        hourNumberPicker.value = 0

        minuteNumberPicker.setFormatter(formatter)
        minuteNumberPicker.minValue = 0
        minuteNumberPicker.maxValue = 59
        minuteNumberPicker.value = 0

        secondNumberPicker.setFormatter(formatterSecond)
        secondNumberPicker.minValue = 0
        secondNumberPicker.maxValue = (60/Global.PERIOD).toInt()-1
        secondNumberPicker.value = 0
    }



    private fun registerReceiver()
    {
        val filter = IntentFilter(Global.TimerBroadcastReceiver)
        context!!.registerReceiver(receiver, filter)
    }

    private fun createTimerSendBroadcast(timerEntity: TimerEntity)
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)

        intent.putExtra(TimerHelper.TimerCreatedFlag,true)
        intent.putExtra(TimerHelper.TimerEntity,timerEntity)

        context!!.sendBroadcast(intent)
    }

    private fun canceledTimerSendBroadcast()
    {
        val intent =  Intent(Global.TimerBroadcastReceiver)
        intent.putExtra(TimerHelper.TimerCanceledFlag,true)
        context!!.sendBroadcast(intent)
    }

//--------------------------------------VISIBLE/GONE------------------------------------------------

    private fun visibleNumberPicker()
    {
        timerFragmentNumberPickerLinearLayout.visibility = View.VISIBLE
        timerFragmentTimerCountDownLinearLayout.visibility = View.GONE
    }

    private fun visibleStartTimerButton()
    {
        timerFragmentStopTimerButton.visibility = View.GONE
        timerFragmentStartTimerButton.visibility = View.VISIBLE
    }

    private fun visibleStopTimerButton()
    {
        timerFragmentStartTimerButton.visibility = View.GONE
        timerFragmentStopTimerButton.visibility = View.VISIBLE
    }

    private fun visibleCountDown()
    {
        timerFragmentTimerCountDownLinearLayout.visibility = View.VISIBLE
        timerFragmentNumberPickerLinearLayout.visibility = View.GONE
    }

    private fun controlSwitchEnable(state:Boolean)
    {
        timerFragmentMuteSwitch.isEnabled = state
    }

}
