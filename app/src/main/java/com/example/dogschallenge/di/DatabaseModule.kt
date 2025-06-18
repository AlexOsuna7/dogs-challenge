package com.example.dogschallenge.di

import android.content.Context
import androidx.room.Room
import com.example.dogschallenge.data.local.DogsDatabase
import com.example.dogschallenge.data.local.dao.DogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): DogsDatabase {
        return Room.databaseBuilder(
            context,
            DogsDatabase::class.java,
            "dog_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideDogDao(database: DogsDatabase): DogDao {
        return database.dogDao()
    }
}
