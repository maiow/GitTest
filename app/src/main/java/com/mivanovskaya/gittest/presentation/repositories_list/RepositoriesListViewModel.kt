package com.mivanovskaya.gittest.presentation.repositories_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.domain.AppRepository
import com.mivanovskaya.gittest.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        getRepositories()
    }

    fun onRetryButtonClick(): Unit = getRepositories()

    fun onLogoutButtonPressed(): Unit = repository.logout()

    private fun getRepositories() {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                _state.value = State.Loading
                val repos = repository.getRepositories(limit = 10, page = 1)
                if (repos.isEmpty())
                    _state.value = State.Empty
                else
                    _state.value = State.Loaded(repos)
            } catch (e: IOException) {
                _state.value = State.NoInternetError
            } catch (e: Exception) {
                _state.value = State.Error(e.message.toString())
            }
        }
    }

    sealed interface State {
        object Loading : State
        object NoInternetError : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }
}