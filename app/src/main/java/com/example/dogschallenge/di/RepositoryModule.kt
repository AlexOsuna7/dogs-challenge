package com.example.dogschallenge.di

import com.example.dogschallenge.data.local.dao.DogDao
import com.example.dogschallenge.data.remote.DogsApiService
import com.example.dogschallenge.data.repository.DogRepositoryImpl
import com.example.dogschallenge.domain.repository.DogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDogRepository(dogsApiService: DogsApiService, dogDao: DogDao): DogRepository {
        return DogRepositoryImpl(dogsApiService, dogDao)
    }
}