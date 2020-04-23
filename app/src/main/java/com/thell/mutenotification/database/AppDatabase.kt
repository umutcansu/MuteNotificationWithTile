package com.thell.mutenotification.database

import android.provider.Settings
import androidx.room.Database
import androidx.room.RoomDatabase
import com.thell.mutenotification.database.dao.NotificationDao
import com.thell.mutenotification.database.dao.SettingsDao
import com.thell.mutenotification.database.dao.TimerDao
import com.thell.mutenotification.database.entity.NotificationEntity
import com.thell.mutenotification.database.entity.SettingsEntity
import com.thell.mutenotification.database.entity.TimerEntity
import com.thell.mutenotification.helper.Global

@Database(  entities = [NotificationEntity::class,SettingsEntity::class,TimerEntity::class],
            version = Global.DATABASE_VERSION)
abstract class AppDatabase: RoomDatabase() {

    abstract fun  getNotificationDao():NotificationDao

    abstract fun  getSettingsDao():SettingsDao

    abstract fun  getTimerDao():TimerDao
}