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
import com.thell.mutenotification.adapter.SettingsAdapter
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.helper.database.DatabaseHelper
import com.thell.mutenotification.helper.navigation.NavigationMenuHelper
import com.thell.mutenotification.helper.callback.IFragmentCommunication
import com.thell.mutenotification.helper.settings.SettingsHelper
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment(val callback: IFragmentCommunication) : Fragment() ,SwipeRefreshLayout.OnRefreshListener
{
    private lateinit var settingsList:List<SettingsEntity>
    private lateinit var adapter : SettingsAdapter
    private lateinit var recycleView : RecyclerView
    private lateinit var searchBox: SearchView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var filter: Filter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_settings, container, false)
        initUI(view)
        init()
        return view
    }

    private  fun initUI(view:View)
    {
        recycleView = view.fragment_settings_RecycleView
        searchBox = view.fragment_settings_SearchView
        swipeRefresh = view.fragment_settings_SwipeRefresh
        swipeRefresh.setOnRefreshListener(this)
        callback.changeHeader(NavigationMenuHelper.SETTING)

    }

    private fun init()
    {
        initFilter()
        initSearchBox()
        settingsList = DatabaseHelper.getInstance(context!!).getSettingsDao().getAll()
        setupRecyclerView()
    }

    private fun clickSettings(settingsEntity: SettingsEntity)
    {
        DatabaseHelper.getInstance(context!!).getSettingsDao().update(settingsEntity.SettingsKey,settingsEntity.State)
        SettingsHelper.setSettingsState(settingsEntity.SettingsKey,settingsEntity.State)
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
        adapter = SettingsAdapter(
            context!!,
            settingsList,
            ::clickSettings
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

                    val resultList = ArrayList<SettingsEntity>()
                    for (d in settingsList)
                    {
                        resultList.add(d)
                    }

                    result.count = resultList.size
                    result.values = resultList
                }
                else
                {
                    val resultList = ArrayList<SettingsEntity>()
                    for (d in settingsList)
                    {
                        if (d.SettingsDescription.toLowerCase().contains(constraint.toString().toLowerCase()))
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
                    adapter = SettingsAdapter(context!!,results.values as List<SettingsEntity>,::clickSettings)
                else
                    adapter = SettingsAdapter(context!!, arrayListOf(),::clickSettings)

                recycleView.adapter = adapter
            }

        }

    }

}
