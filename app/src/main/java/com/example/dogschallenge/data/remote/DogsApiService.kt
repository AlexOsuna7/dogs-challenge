package com.example.dogschallenge.data.remote

import com.example.dogschallenge.data.remote.dto.DogDto
import retrofit2.http.GET

/**
 * Retrofit API service interface.
 */
interface DogsApiService {
    @GET("api/1151549092634943488")
    suspend fun getDogs(): List<DogDto>
}