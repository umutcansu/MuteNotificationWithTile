package com.thell.mutenotification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thell.mutenotification.R
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.helper.GuiHelper
import kotlinx.android.synthetic.main.notification_history_item_layout.view.*
import java.time.LocalDateTime
import java.util.zip.Inflater

class NotificationHistoryAdapter(val context:Context,val data:List<NotificationEntity>,val clickListener : (current:NotificationEntity) -> Unit = {})
    :RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.notification_history_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
       holder.setData(data[position])
    }

    inner class ViewHolder(val item:View) : RecyclerView.ViewHolder(item)
    {

        fun setData(notificationEntity: NotificationEntity)
        {
            item.notification_history_icon_imageview.background = GuiHelper.getIcon(context,notificationEntity)
            item.notification_history_package_name_textview.text = notificationEntity.ApplicationName
            item.notification_history_notification_context_textview.text = notificationEntity.Ticket

            var postTime = GuiHelper.epochToDate(notificationEntity.PostTime)
            item.notification_history_notification_posttime_textview.text = postTime

            if(notificationEntity.MuteState)
                item.notification_history_mutestate_imageview.setImageResource(R.drawable.ic_notifications_off_black_24dp)
            else
                item.notification_history_mutestate_imageview.setImageResource(R.drawable.ic_notifications_black_24dp)

            item.rootView.setOnClickListener {
                clickListener(notificationEntity)
            }
        }
    }
}