package com.thell.mutenotification.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thell.mutenotification.helper.settings.SettingsStateType

@Entity(tableName = "Settings")
data class SettingsEntity
(
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0,
    @ColumnInfo(name = "SettingsKey")
    var SettingsKey: String = "",
    @ColumnInfo(name = "SettingsDescription")
    var SettingsDescription: String = "",
    @ColumnInfo(name = "State")
    var State: Int = SettingsStateType.NA.state
)
