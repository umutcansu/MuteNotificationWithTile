package com.thell.mutenotification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.thell.mutenotification.R
import com.thell.mutenotification.adapter.NavigationDrawerAdapter
import com.thell.mutenotification.model.NavigationDrawerItem
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_navigation_drawer.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.thell.mutenotification.helper.navigation.NavigationMenuHelper


class NavigationDrawerFragment() : Fragment()
{

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationDrawerRecyclerView: RecyclerView
    private lateinit var adapter:NavigationDrawerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        navigationDrawerRecyclerView =  v.navigationDrawerRecyclerView
        return v
    }

    fun setupDrawerToggle(drawerLayout: DrawerLayout, toolbar: Toolbar,
                          menuChangeListener : (menu:NavigationDrawerItem) -> Unit ={}) {

        mDrawerToggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar,
            R.string.toolbar_open,
            R.string.toolbar_close
        )
        setupRecyclerView(menuChangeListener)

        drawerLayout.post()
        {
            //mDrawerToggle.syncState()
        }
    }



    fun setupRecyclerView(menuChangeListener : (menu:NavigationDrawerItem) -> Unit ={})
    {
        adapter = NavigationDrawerAdapter(
            navigationDrawerRecyclerView.context,
            NavigationMenuHelper.allMenuItem,
            menuChangeListener
        )
        navigationDrawerRecyclerView.layoutManager = LinearLayoutManager(
            navigationDrawerRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        navigationDrawerRecyclerView.adapter = adapter
    }


}
