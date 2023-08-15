package com.mivanovskaya.gittest.presentation.repositories_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.domain.AppRepository
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.presentation.tools.StringValue
import com.mivanovskaya.gittest.presentation.tools.requestWithErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var job: Job? = null

    init {
        getRepositories()
    }

    fun onRetryButtonClick(): Unit = getRepositories()

    fun onLogoutButtonPressed(): Unit = repository.logout()

    private fun getRepositories() {
        job?.cancel()
        job = viewModelScope.launch {
            requestWithErrorHandling(
                block = {
                    _state.value = State.Loading
                    val repos = repository.getRepositories(limit = 10, page = 1)
                    if (repos.isEmpty())
                        _state.value = State.Empty
                    else
                        _state.value = State.Loaded(repos)
                },
                errorFactory = State::Error,
                setState = { _state.value = it }
            )
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val isNetworkError: Boolean, val error: StringValue) : State
        object Empty : State
    }
}