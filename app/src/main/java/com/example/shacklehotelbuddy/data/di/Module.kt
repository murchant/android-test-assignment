package com.example.shacklehotelbuddy.data.di

import android.content.Context
import androidx.room.Room
import com.example.shacklehotelbuddy.data.db.AppDatabase
import com.example.shacklehotelbuddy.data.db.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "search-parameters"
        ).build()
    }

    @Singleton
    @Provides
    fun provideEntitlementDao(db: AppDatabase) = db.searchDao()
}