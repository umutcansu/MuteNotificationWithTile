package com.thell.mutenotification.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thell.mutenotification.database.dao.NotificationDao
import com.thell.mutenotification.database.entity.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun  getNotificationDao():NotificationDao
}