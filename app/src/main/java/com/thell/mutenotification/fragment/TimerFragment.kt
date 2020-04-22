package com.thell.mutenotification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.thell.mutenotification.R
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import com.thell.mutenotification.model.NavigationDrawerItem


class TimerFragment(val callback: IFragmentCommunication) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_timer, container, false)
        initUI(view)
        init()
        return view
    }

    private  fun initUI(view:View)
    {
        callback.changeHeader(NavigationDrawerItem.TIMER)
    }

    private  fun init()
    {

    }

}
