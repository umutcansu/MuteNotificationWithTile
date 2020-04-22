package com.thell.mutenotification.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thell.mutenotification.database.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert
    fun insert(value:NotificationEntity)

    @Query("select * from Notification order by PostTime desc")
    fun getAll():List<NotificationEntity>

}