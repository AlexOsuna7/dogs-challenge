package com.example.dogschallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dogschallenge.data.local.entity.DogEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the dogs table.
 */
@Dao
interface DogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dogs: List<DogEntity>)

    @Query("SELECT * FROM dogs")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("DELETE FROM dogs")
    suspend fun clearAll()
}
