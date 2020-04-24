package com.thell.mutenotification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thell.mutenotification.R
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.helper.GuiHelper
import kotlinx.android.synthetic.main.fragment_bottom_sheet_history_item_details.view.*
import kotlinx.android.synthetic.main.notification_history_item_layout.view.*


class HistoryDetailBottomSheetDialogFragment(var notificationEntity: NotificationEntity): BottomSheetDialogFragment()
{



    lateinit var historyDetailFragmentIcon:ImageView
    lateinit var historyDetailFragmentPackageName:TextView
    lateinit var historyDetailFragmentDetailContent:TextView
    lateinit var historyDetailFragmentPostTime:TextView
    lateinit var historyDetailFragmentMuteStateIcon:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        var view = inflater.inflate(R.layout.fragment_bottom_sheet_history_item_details,container,false)

        initUI(view)
        init()

        return view
    }

    private fun init()
    {
        historyDetailFragmentIcon.background = GuiHelper.getIcon(context!!,notificationEntity)
        historyDetailFragmentPackageName.text = notificationEntity.ApplicationName
        historyDetailFragmentDetailContent.text =  notificationEntity.Ticket
        var postTime = GuiHelper.epochToDate(notificationEntity.PostTime)
        historyDetailFragmentPostTime.text = postTime

        if(notificationEntity.MuteState)
            historyDetailFragmentMuteStateIcon.setImageResource(R.drawable.ic_notifications_off_black_24dp)
        else
            historyDetailFragmentMuteStateIcon.setImageResource(R.drawable.ic_notifications_black_24dp)
    }

    private fun initUI(view: View)
    {
        historyDetailFragmentIcon =  view.notification_history_detail_icon_imageview
        historyDetailFragmentPackageName = view.notification_history_detail_package_name_textview
        historyDetailFragmentDetailContent = view.notification_history_detail_content_textview
        historyDetailFragmentPostTime =  view.notification_history_detail_posttime_textview
        historyDetailFragmentMuteStateIcon = view.notification_history_detail_mutestate_imageview

    }
}