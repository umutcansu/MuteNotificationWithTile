package com.thell.mutenotification.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thell.mutenotification.database.entity.SettingsEntity

@Dao
interface SettingsDao
{
    @Insert
    fun insert(value: SettingsEntity)

    @Query("update Settings set  State = :state where SettingsKey = :key ")
    fun update(key:String,state:Int)

    @Query("select * from Settings")
    fun getAll():List<SettingsEntity>

    @Query("select * from Settings where SettingsKey = :key")
    fun getBySettingsKey(key:String):SettingsEntity?

}