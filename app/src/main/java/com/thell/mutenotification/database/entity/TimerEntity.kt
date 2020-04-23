package com.thell.mutenotification.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thell.mutenotification.helper.settings.SettingsStateType

@Entity(tableName = "Timer")
data class TimerEntity
    (
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0,
    @ColumnInfo(name = "CreatedDatetime")
    var CreatedDatetime: Long = 0,
    @ColumnInfo(name = "TimerTime")
    var TimerTime: Long = 0,
    @ColumnInfo(name = "State")
    var State: Boolean = false,
    @ColumnInfo(name = "IsFinish")
    var IsFinish: Boolean = false
)