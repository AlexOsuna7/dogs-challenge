package com.example.dogschallenge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database Entity for storing dog data.
 */
@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dogName: String,
    val description: String,
    val age: Int,
    val imageUrl: String
)