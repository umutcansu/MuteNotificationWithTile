package com.thell.mutenotification.helper

import android.content.Context
import androidx.room.Room
import com.thell.mutenotification.database.AppDatabase

class DatabaseHelper private constructor()
{
    companion object {
        private lateinit var instance : AppDatabase

        fun getInstance(context:Context):AppDatabase
        {
            if(!::instance.isInitialized){
                instance = Room.databaseBuilder(context, AppDatabase::class.java, Global.DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return instance
        }
    }
}