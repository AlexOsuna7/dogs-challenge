package com.example.dogschallenge.domain.repository

import com.example.dogschallenge.domain.model.Dog
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Dog Repository.
 * It defines the contract for data operations related to Dogs, abstracting away
 * the data source details (network vs. local database).
 * This belongs to the domain layer.
 */
interface DogRepository {
    /**
     * Fetches a list of dogs.
     * The implementation will handle the logic of fetching from the network and/or caching.
     * @return A Flow that emits a list of Dogs.
     */
    fun getDogs(): Flow<List<Dog>>
}