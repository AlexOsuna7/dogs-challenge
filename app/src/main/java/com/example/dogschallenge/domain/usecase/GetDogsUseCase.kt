package com.example.dogschallenge.domain.usecase

import com.example.dogschallenge.domain.model.Dog
import com.example.dogschallenge.domain.repository.DogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the list of dogs.
 * This class encapsulates a single business operation. It depends on the repository
 * interface, not the implementation, adhering to Clean Architecture principles.
 */
class GetDogsUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {
    operator fun invoke(): Flow<List<Dog>> {
        return dogRepository.getDogs()
    }
}