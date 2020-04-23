package com.thell.mutenotification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thell.mutenotification.R
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.helper.settings.SettingsStateType
import kotlinx.android.synthetic.main.settings_item_layout.view.*

class SettingsAdapter(val context:Context,val data:List<SettingsEntity>,val clickListener : (current:SettingsEntity) -> Unit = {}) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.settings_item_layout,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.setData(data[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun setData(item:SettingsEntity)
        {
            itemView.settings_item_name_textview.text = item.SettingsDescription
            itemView.settings_item_state_checkbox.isChecked = item.State == SettingsStateType.OK.state

            itemView.settings_item_state_checkbox.setOnCheckedChangeListener { _, _ ->
                clickBehavior(item)
            }

     /*       itemView.rootView.setOnClickListener {

                clickBehavior(item)
            }*/
        }

        fun clickBehavior(item: SettingsEntity)
        {
            var state = SettingsStateType.NA.state

            if(item.State == SettingsStateType.OK.state)
                state = SettingsStateType.NOK.state
            else if(item.State == SettingsStateType.NOK.state)
                state = SettingsStateType.OK.state

            item.State = state
            itemView.settings_item_state_checkbox.isChecked = item.State == SettingsStateType.OK.state
            clickListener(item)
        }
    }
}