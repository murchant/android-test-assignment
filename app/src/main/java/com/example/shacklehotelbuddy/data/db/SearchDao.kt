package com.example.shacklehotelbuddy.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shacklehotelbuddy.MainViewModel

@Dao
interface SearchDao {
    @Query("SELECT * FROM searchparameter")
    fun getAll(): List<MainViewModel.SearchParameter>

    @Insert
    fun insert(entry: MainViewModel.SearchParameter)
}