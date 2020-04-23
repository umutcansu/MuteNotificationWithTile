package com.thell.mutenotification.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thell.mutenotification.database.entity.TimerEntity

@Dao
interface TimerDao
{
    @Insert
    fun insert(value: TimerEntity)

    @Query("update Timer set  IsFinish = :isFinish where ID = :id ")
    fun update(id:Int,isFinish:Boolean)

    @Query("select * from Timer")
    fun getAll():List<TimerEntity>

    @Query("select  * from Timer where IsFinish = :state ")
    fun getLastTimer(state:Boolean = false):TimerEntity
}

