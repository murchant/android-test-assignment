package com.example.shacklehotelbuddy.data.db

import androidx.room.Database
import com.example.shacklehotelbuddy.MainViewModel

@Database(entities = [MainViewModel.SearchParameter::class], version = 1)
abstract class AppDatabase : androidx.room.RoomDatabase() {
    abstract fun searchDao(): SearchDao
}