package com.example.dogschallenge.presentation

import app.cash.turbine.test
import com.example.dogschallenge.domain.model.Dog
import com.example.dogschallenge.domain.usecase.GetDogsUseCase
import com.example.dogschallenge.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DogListViewModelTest {

    // We use a TestRule to safely manage the dispatcher.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks for our dependencies
    private lateinit var getDogsUseCase: GetDogsUseCase
    private lateinit var viewModel: DogListViewModel

    // Test data
    private val fakeDogs = listOf(
        Dog("Rex", "A good boy", 5, "url1"),
        Dog("Buddy", "A fluffy boy", 3, "url2")
    )

    @Before
    fun setUp() {
        getDogsUseCase = mock()
    }

    @Test
    fun `when use case returns dogs, state transitions from Loading to Success`() = runTest {
        // Arrange
        whenever(getDogsUseCase.invoke()).thenReturn(flowOf(fakeDogs))

        // Act & Assert
        // We create the ViewModel inside the test for greater control.
        viewModel = DogListViewModel(getDogsUseCase)
        viewModel.state.test {
            // The first item emitted should be the initial 'Loading' value
            assertEquals(DogListState.Loading, awaitItem())

            // The next item emitted should be 'Success' after the init coroutine runs.
            val successState = awaitItem()
            assertTrue(successState is DogListState.Success)
            assertEquals(fakeDogs, (successState as DogListState.Success).dogs)

            // We make sure no more items are emitted
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case throws an exception, state transitions from Loading to Error`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        whenever(getDogsUseCase.invoke()).thenReturn(flow { throw RuntimeException(errorMessage) })

        // Act
        viewModel = DogListViewModel(getDogsUseCase)

        // Assert
        viewModel.state.test {
            assertEquals(DogListState.Loading, awaitItem())

            val errorState = awaitItem()
            assertTrue(errorState is DogListState.Error)
            assertEquals(errorMessage, (errorState as DogListState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemImageLoaded adds the item to the loaded set`() = runTest {
        // Arrange
        whenever(getDogsUseCase.invoke()).thenReturn(flowOf(fakeDogs))
        viewModel = DogListViewModel(getDogsUseCase)
        val dogId = "Rex5" // Unique key as in the UI

        // Act & Assert
        viewModel.loadedItems.test {
            // The initial state is an empty set
            assertEquals(emptySet<String>(), awaitItem())

            // We mark an item as loaded
            viewModel.onItemImageLoaded(dogId)

            // The new state must contain the dog's ID
            val loadedSet = awaitItem()
            assertTrue(loadedSet.contains(dogId))
            assertEquals(1, loadedSet.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}