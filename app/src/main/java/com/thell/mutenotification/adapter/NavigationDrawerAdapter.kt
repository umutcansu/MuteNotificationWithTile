package com.thell.mutenotification.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thell.mutenotification.R
import com.thell.mutenotification.model.NavigationDrawerItem
import kotlinx.android.synthetic.main.navigation_drawer_item_layout.view.*

class NavigationDrawerAdapter(val context: Context, val data: ArrayList<NavigationDrawerItem>) :
    RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)

        val v = inflater.inflate(R.layout.navigation_drawer_item_layout, p0, false)

        val holder = NavigationDrawerAdapter.ViewHolder(v)

        return holder

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int)
    {
        val current = data[p1]
        p0.setData(current)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun setData(current: NavigationDrawerItem) {
            itemView.navigationDrawerItemIcon.setImageResource(current.icon)
            itemView.navigationDrawerItemText.text = current.title

            itemView.setOnClickListener{

                when(current.title)
                {
                    NavigationDrawerItem.SETTING -> Log.e("","")

                }
            }
        }
    }
}