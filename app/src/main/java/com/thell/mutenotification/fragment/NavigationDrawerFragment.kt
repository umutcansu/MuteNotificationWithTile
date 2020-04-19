package com.thell.mutenotification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle

import com.thell.mutenotification.R
import com.thell.mutenotification.adapter.NavigationDrawerAdapter
import com.thell.mutenotification.model.NavigationDrawerItem
import kotlinx.android.synthetic.main.fragment_navigation_drawer.view.*


class NavigationDrawerFragment : Fragment() {

    lateinit var mDrawerToogle: ActionBarDrawerToggle
    var selected = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        setupRecylerView(v.navigationDrawerRecyclerView)

        return v
    }

    fun setupDrawertoogle(drawerLayout: androidx.drawerlayout.widget.DrawerLayout, toolbar: androidx.appcompat.widget.Toolbar) {
        mDrawerToogle = ActionBarDrawerToggle(activity, drawerLayout, toolbar,
            R.string.toolbar_open,
            R.string.toolbar_close
        )
        drawerLayout.post()
        {
            //mDrawerToogle.syncState()
        }
    }

    private fun setupRecylerView(rc: androidx.recyclerview.widget.RecyclerView)
    {
        val adapter = NavigationDrawerAdapter(rc.context,
            NavigationDrawerItem.allMenuItem()
        )
        rc.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            rc.context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        rc.adapter =adapter
    }
}
