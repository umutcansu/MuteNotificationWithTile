package com.thell.mutenotification.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.thell.mutenotification.R
import com.thell.mutenotification.broadcastreceiver.NotificationServiceBroadcastReceiver
import com.thell.mutenotification.helper.Global
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.NotificationServiceHelper
import com.thell.mutenotification.helper.mutestate.IMuteStateAction
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment()
{

//---------------------------------------------------------------------------------------------

    private val switchChange = object : CompoundButton.OnCheckedChangeListener
    {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean)
        {
            if (::muteStateAction.isInitialized)
                muteStateAction.switchMuteState()
        }
    }

    private val receiver = object : NotificationServiceBroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            super.onReceive(p0, p1)
            val state = p1!!.getBooleanExtra(Global.MUTE_STATE_KEY,false)
            checkNotificationState(state)
        }
    }



//----------------------------------------------------------------------------------------------

    private lateinit var muteStateAction : IMuteStateAction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_main, container, false)
        initUI(view)
        init()
        return view
    }


    private fun init()
    {
        val filter = IntentFilter(Global.NotificationServiceBroadcastReceiver)
        context?.registerReceiver(receiver, filter)
        muteStateAction = Global.getMuteStateAction(context!!)
    }

    private lateinit var mainFragmentHeaderTextView :TextView
    private lateinit var mainFragmentMuteStateTextView :TextView
    private lateinit var mainFragmentMuteStateExpTextView :TextView
    private lateinit var mainFragmentMuteSwitch :ToggleButton

    private fun initUI(view: View)
    {
        mainFragmentHeaderTextView = view.mainFragmentHeaderTextView
        mainFragmentMuteStateTextView = view.mainFragmentMuteStateTextView
        mainFragmentMuteStateExpTextView = view.mainFragmentMuteStateExpTextView
        mainFragmentMuteSwitch = view.mainFragmentMuteSwitch

        GuiHelper.setTextViewPatternBackground(resources,R.drawable.pattern,mainFragmentHeaderTextView)
        setStateInit()
    }

    private fun checkNotificationState(state:Boolean)
    {
        mainFragmentMuteSwitch.setOnCheckedChangeListener(null)
        mainFragmentMuteSwitch.isChecked = state
        mainFragmentMuteSwitch.setOnCheckedChangeListener(switchChange)
        setState(state)
    }

    private  fun setStateInit()
    {
        val state = Global.getMuteStateAction(context!!).getMuteState()
        checkNotificationState(state)
    }

    private  fun setState(state:Boolean)
    {
        mainFragmentMuteStateTextView.apply {
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

        mainFragmentMuteStateExpTextView.apply {

            val sp : Spanned = when(state)
            {
                true -> HtmlCompat.fromHtml(getString(R.string.muteexp), HtmlCompat.FROM_HTML_MODE_LEGACY)
                else -> HtmlCompat.fromHtml(getString(R.string.notificationexp), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            text = sp

        }

    }

}
