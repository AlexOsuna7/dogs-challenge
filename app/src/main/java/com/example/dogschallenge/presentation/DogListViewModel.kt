package com.example.dogschallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogschallenge.domain.usecase.GetDogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Manages the state for the DogListScreen. It exposes a StateFlow
 * of [DogListState] to the UI, representing all possible states
 * (Loading, Success, Error).
 */
@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogsUseCase: GetDogsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DogListState>(DogListState.Loading)
    val state: StateFlow<DogListState> = _state

    private val _loadedItems = MutableStateFlow<Set<String>>(emptySet())
    val loadedItems: StateFlow<Set<String>> = _loadedItems

    init {
        fetchDogs()
    }

    private fun fetchDogs() {
        getDogsUseCase()
            .onEach { dogs ->
                _state.value = if (dogs.isEmpty()) {
                    DogListState.Loading
                } else {
                    DogListState.Success(dogs)
                }
            }
            .catch { e ->
                _state.value = DogListState.Error(e.message ?: "An unexpected error occurred")
            }
            .launchIn(viewModelScope)
    }

    fun onRetry() {
        fetchDogs()
    }

    fun onItemImageLoaded(dogName: String) {
        _loadedItems.value += dogName
    }
}