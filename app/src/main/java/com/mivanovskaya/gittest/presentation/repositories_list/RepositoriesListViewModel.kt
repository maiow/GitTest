package com.mivanovskaya.gittest.presentation.repositories_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.data.AppRepository
import com.mivanovskaya.gittest.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        getRepositories()
    }

    fun getRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = State.Loading
                delay(2000)
                val repos = repository.getRepositories()
                if (repos.isEmpty())
                    _state.value = State.Empty
                else
                    _state.value = State.Loaded(repos)
            } catch (e: Exception) {
                _state.value = State.Error(e.message.toString())
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }
}