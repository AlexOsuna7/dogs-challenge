package com.example.dogschallenge.di

import com.example.dogschallenge.data.remote.DogsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): DogsApiService {
        return Retrofit.Builder()
            .baseUrl("https://jsonblob.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogsApiService::class.java)
    }
}
