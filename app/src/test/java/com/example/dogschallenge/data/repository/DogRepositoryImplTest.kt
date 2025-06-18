package com.example.dogschallenge.data.repository

import com.example.dogschallenge.data.local.dao.DogDao
import com.example.dogschallenge.data.local.entity.DogEntity
import com.example.dogschallenge.data.remote.DogsApiService
import com.example.dogschallenge.data.remote.dto.DogDto
import com.example.dogschallenge.data.remote.mapper.toEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class DogRepositoryImplTest {

    private lateinit var apiService: DogsApiService
    private lateinit var dogDao: DogDao
    private lateinit var repository: DogRepositoryImpl

    // Test data for the API and DB
    private val fakeApiDogs = listOf(DogDto("Rex", "API dog", 5, "url_api"))
    private val fakeDbDogs = listOf(DogEntity(id = 1, dogName = "Buddy", description = "DB dog", age = 3, imageUrl = "url_db"))

    @Before
    fun setUp() {
        apiService = mock()
        dogDao = mock()
        repository = DogRepositoryImpl(apiService, dogDao)
    }

    @Test
    fun `getDogs - when DB is empty, fetches from API and inserts into DB`() = runTest {
        // Arrange
        // The DAO will first return an empty list
        whenever(dogDao.getAllDogs()).thenReturn(flowOf(emptyList()))
        // The API will return its list of dogs
        whenever(apiService.getDogs()).thenReturn(fakeApiDogs)

        // Act
        // We collect the first emission. The logic inside map() will execute.
        val result = repository.getDogs().first()

        // Assert
        // 1. Verify that the API was called.
        verify(apiService).getDogs()

        // 2. Verify that the result from the API was inserted into the DAO.
        verify(dogDao).insertAll(fakeApiDogs.map { it.toEntity() })

        // 3. The result of the flow should be the (initially) empty list from the DB.
        // A real UI would receive a second emission with the new data.
        assertEquals(emptyList<DogEntity>(), result)
    }

    @Test
    fun `getDogs - when DB is not empty, returns data from DB and does NOT call API`() = runTest {
        // Arrange
        // The DAO will return a list with data
        whenever(dogDao.getAllDogs()).thenReturn(flowOf(fakeDbDogs))

        // Act
        val result = repository.getDogs().first()

        // Assert
        // 1. Verify that the API was NEVER called.
        verify(apiService, never()).getDogs()

        // 2. Verify the result is the mapped data from the database.
        assertEquals(fakeDbDogs.size, result.size)
        assertEquals("Buddy", result.first().dogName)
    }
}