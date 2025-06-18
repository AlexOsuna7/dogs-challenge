package com.example.dogschallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogschallenge.data.local.dao.DogDao
import com.example.dogschallenge.data.local.entity.DogEntity

@Database(entities = [DogEntity::class], version = 1, exportSchema = false)
abstract class DogsDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
}