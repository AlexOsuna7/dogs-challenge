package com.example.dogschallenge.data.local.mapper

import com.example.dogschallenge.data.local.entity.DogEntity
import com.example.dogschallenge.domain.model.Dog

fun DogEntity.toDomain(): Dog {
    return Dog(
        dogName = this.dogName,
        description = this.description,
        age = this.age,
        imageUrl = this.imageUrl
    )
}