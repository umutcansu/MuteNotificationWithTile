package com.thell.mutenotification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.thell.mutenotification.R
import com.thell.mutenotification.adapter.NotificationHistoryAdapter
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.helper.NavigationMenuHelper
import com.thell.mutenotification.helper.database.DatabaseHelper
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import com.thell.mutenotification.model.NavigationDrawerItem
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment(val callback:IFragmentCommunication) : Fragment() ,SwipeRefreshLayout.OnRefreshListener{

    private lateinit var notificationList:List<NotificationEntity>
    private lateinit var adapter :NotificationHistoryAdapter
    private lateinit var recycleView : RecyclerView
    private lateinit var searchBox: SearchView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var filter: Filter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        initUI(view)
        init()

        return view
    }

    private  fun initUI(view:View)
    {
        recycleView = view.fragment_notification_history_RecycleView
        searchBox = view.fragment_notification_history_SearchView
        swipeRefresh = view.fragment_notification_history_SwipeRefresh
        swipeRefresh.setOnRefreshListener(this)
        callback.changeHeader(NavigationMenuHelper.HISTORY)

    }

    private fun init()
    {
        initFilter()
        initSearchBox()
        notificationList = DatabaseHelper.getInstance(context!!).getNotificationDao().getAll()
        setupRecyclerView()
    }

    private fun clickNotification(notificationEntity: NotificationEntity)
    {

    }

    private fun initSearchBox()
    {
        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter.filter(newText)
                return false
            }

        })
    }

    private fun setupRecyclerView()
    {
        adapter = NotificationHistoryAdapter(
            context!!,
            notificationList,
            ::clickNotification
        )
        recycleView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recycleView.adapter = adapter
    }

    override fun onRefresh()
    {
        searchBox.setQuery("",false)
        init()
        swipeRefresh.isRefreshing = false
    }


    private fun initFilter()
    {
        filter = object : android.widget.Filter()
        {
            override fun performFiltering(constraint: CharSequence?): FilterResults
            {
                val result = FilterResults()

                if (constraint.isNullOrEmpty())
                {

                    val resultList = ArrayList<NotificationEntity>()
                    for (d in notificationList)
                    {
                        resultList.add(d)
                    }

                    result.count = resultList.size
                    result.values = resultList
                }
                else
                {
                    val resultList = ArrayList<NotificationEntity>()
                    for (d in notificationList)
                    {
                        if (d.ApplicationName.toLowerCase().contains(constraint.toString().toLowerCase()))
                            resultList.add(d)

                    }

                    result.count = resultList.size
                    result.values = resultList

                }

                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?)
            {
                @Suppress("UNCHECKED_CAST")
                if(results?.values != null)
                    adapter = NotificationHistoryAdapter(context!!,results.values as List<NotificationEntity>,::clickNotification)
                else
                    adapter = NotificationHistoryAdapter(context!!, arrayListOf(),::clickNotification)

                recycleView.adapter = adapter
            }

        }

    }

}
