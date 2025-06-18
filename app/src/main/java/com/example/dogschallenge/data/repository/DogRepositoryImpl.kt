package com.example.dogschallenge.data.repository

import com.example.dogschallenge.data.local.dao.DogDao
import com.example.dogschallenge.data.local.mapper.toDomain
import com.example.dogschallenge.data.remote.DogsApiService
import com.example.dogschallenge.data.remote.mapper.toEntity
import com.example.dogschallenge.domain.model.Dog
import com.example.dogschallenge.domain.repository.DogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the DogRepository.
 * This class implements the "offline-first" strategy.
 */
class DogRepositoryImpl @Inject constructor(
    private val apiService: DogsApiService,
    private val dogDao: DogDao
) : DogRepository {

    override fun getDogs(): Flow<List<Dog>> {
        return dogDao.getAllDogs().map { dogsFromDb ->
            if (dogsFromDb.isEmpty()) {
                try {
                    val dogsFromApi = apiService.getDogs()
                    dogDao.insertAll(dogsFromApi.map { it.toEntity() })
                } catch (e: Exception) {
                    // If the database is empty and the network call fails, we have no data to show.
                    // We propagate the exception up to the ViewModel so it can be handled in the UI
                    // (e.g., show an error message).
                    throw e
                }
            }
            dogsFromDb.map { it.toDomain() }
        }
    }
}