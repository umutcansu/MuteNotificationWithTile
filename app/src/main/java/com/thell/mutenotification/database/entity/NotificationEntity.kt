package com.thell.mutenotification.database.entity

import androidx.room.*


@Entity(tableName = "Notification")
data class NotificationEntity
(
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    @ColumnInfo(name = "PackageName")
    val PackageName: String = "",
    @ColumnInfo(name = "IconId")
    val IconId: String=""
)
