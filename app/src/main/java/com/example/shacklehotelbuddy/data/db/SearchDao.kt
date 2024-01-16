package com.example.shacklehotelbuddy.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shacklehotelbuddy.data.SearchParameter

@Dao
interface SearchDao {
    @Query("SELECT * FROM searchparameter")
    fun getAll(): List<SearchParameter>

    @Insert
    fun insert(entry: SearchParameter)
}