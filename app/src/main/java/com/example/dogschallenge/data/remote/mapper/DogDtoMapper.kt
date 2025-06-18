package com.example.dogschallenge.data.remote.mapper

import com.example.dogschallenge.data.local.entity.DogEntity
import com.example.dogschallenge.data.remote.dto.DogDto
import com.example.dogschallenge.domain.model.Dog

fun DogDto.toDomain(): Dog {
    return Dog(
        dogName = this.dogName,
        description = this.description,
        age = this.age,
        imageUrl = this.imageUrl
    )
}

fun DogDto.toEntity(): DogEntity {
    return DogEntity(
        dogName = this.dogName,
        description = this.description,
        age = this.age,
        imageUrl = this.imageUrl
    )
}