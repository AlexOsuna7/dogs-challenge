package com.example.dogschallenge.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for the dog data coming from the network API.
 * This is used by Retrofit to parse the JSON response.
 */
data class DogDto(
    @SerializedName("dogName") val dogName: String,
    @SerializedName("description") val description: String,
    @SerializedName("age") val age: Int,
    @SerializedName("image") val imageUrl: String
)