package com.thell.mutenotification.helper.database

import android.content.Context
import androidx.room.Room
import com.thell.mutenotification.database.AppDatabase
import com.thell.mutenotification.helper.Global

class DatabaseHelper private constructor()
{
    companion object {
        private lateinit var instance : AppDatabase

        fun getInstance(context:Context):AppDatabase
        {
            if(!Companion::instance.isInitialized){
                instance = Room.databaseBuilder(context, AppDatabase::class.java,
                    Global.DATABASE_NAME
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return instance
        }


    }
}