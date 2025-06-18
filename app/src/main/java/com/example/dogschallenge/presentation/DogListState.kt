package com.example.dogschallenge.presentation

import com.example.dogschallenge.domain.model.Dog

/**
 * Represents the different states of the Dog List UI.
 */
sealed class DogListState {
    object Loading : DogListState()
    data class Success(val dogs: List<Dog>) : DogListState()
    data class Error(val message: String) : DogListState()
}