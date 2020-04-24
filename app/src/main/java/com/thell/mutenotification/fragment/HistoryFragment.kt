package com.thell.mutenotification.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageButton
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.thell.mutenotification.R
import com.thell.mutenotification.adapter.NotificationHistoryAdapter
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.helper.GuiHelper
import com.thell.mutenotification.helper.navigation.NavigationMenuHelper
import com.thell.mutenotification.helper.database.DatabaseHelper
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment(val callback:IFragmentCommunication) : Fragment() ,SwipeRefreshLayout.OnRefreshListener{

    private lateinit var notificationList:List<NotificationEntity>
    private lateinit var adapter :NotificationHistoryAdapter
    private lateinit var recycleView : RecyclerView
    private lateinit var searchBox: SearchView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var clearButton: ImageButton
    private lateinit var filter: Filter
    private lateinit var bottomSheetDialogFragment :BottomSheetDialogFragment


    private val clearOnClick = object :View.OnClickListener
    {
        lateinit var dialog: AlertDialog
        lateinit var alertDialogBuilder: AlertDialog.Builder

        override fun onClick(p0: View)
        {
            GuiHelper.startRotatingView(null,p0,::coreClick)
        }

        private fun coreClick()
        {
            if(!::notificationList.isInitialized || notificationList.isEmpty())
                return

            if(!::alertDialogBuilder.isInitialized)
            {
                val message = getString(R.string.clearAllHistory)

                alertDialogBuilder = AlertDialog.Builder(this@HistoryFragment.context!!)
                alertDialogBuilder.setTitle(R.string.app_name)
                alertDialogBuilder.setMessage(message)
                alertDialogBuilder.setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, _ ->
                        clearHistory()
                        dialog.dismiss()
                    })
                alertDialogBuilder.setNegativeButton(
                    R.string.no,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            }
            if(!::dialog.isInitialized)
            {
                dialog = alertDialogBuilder.create()
            }

            dialog.show()

        }
    }


    private fun clearHistory()
    {
        DatabaseHelper.getInstance(context!!).getNotificationDao().clearAll()
        init()
    }


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
        clearButton = view.fragment_notification_history_clear_button
        clearButton.setOnClickListener(clearOnClick)
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
        bottomSheetDialogFragment = HistoryDetailBottomSheetDialogFragment(notificationEntity)
        bottomSheetDialogFragment.show(fragmentManager!!,"frag")
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
