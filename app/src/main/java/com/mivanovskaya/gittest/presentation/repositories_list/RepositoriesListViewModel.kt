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

    init {
        getRepositories()
    }

    fun onRetryButtonClick() = getRepositories()

    fun onLogoutButtonPressed() = repository.logout()

    private fun getRepositories() {
        val job = Job()
        viewModelScope.launch(job) {
            try {
                _state.value = State.Loading
                val repos = repository.getRepositories()
                if (repos.isEmpty())
                    _state.value = State.Empty
                else
                    _state.value = State.Loaded(repos)
            } catch (e: IOException) {
                handleNetworkException()
            } catch (e: Exception) {
                _state.value = State.Error(e.message.toString())
            }
            job.cancel()
        }
    }

    private fun handleNetworkException() {
            _state.value = State.Error(NO_INTERNET)
        }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    companion object {
        const val NO_INTERNET = "NO_INTERNET"
    }
}