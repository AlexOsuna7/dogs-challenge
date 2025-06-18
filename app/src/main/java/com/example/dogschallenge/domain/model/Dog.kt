package com.example.dogschallenge.domain.model

/**
 * Represents the core business model for a Dog.
 * This class is framework-agnostic and part of the domain layer.
 */
data class Dog(
    val dogName: String,
    val description: String,
    val age: Int,
    val imageUrl: String
)
