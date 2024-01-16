package com.example.shacklehotelbuddy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchParameter(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "adult") var adult: Int,
    @ColumnInfo(name="children") var children: Int,
    @ColumnInfo(name="checkInDate") var checkInDate: String,
    @ColumnInfo(name="checkOutDate") var checkOutDate: String,
)
