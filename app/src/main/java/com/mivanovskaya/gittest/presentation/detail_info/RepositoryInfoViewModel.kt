package com.mivanovskaya.gittest.presentation.detail_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.domain.AppRepository
import com.mivanovskaya.gittest.domain.model.RepoDetails
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RepositoryInfoViewModel @AssistedInject constructor(
    private val repository: AppRepository,
    @Assisted private val repoName: String
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _readmeState = MutableStateFlow<ReadmeState>(ReadmeState.Loading)

    private var job: Job? = null

    init {
        getRepoInfo(repoName)
    }

    fun onLogoutButtonPressed(): Unit = repository.logout()

    fun onRetryButtonClick(): Unit = getRepoInfo(repoName)

    private fun getRepoInfo(repoName: String) {
        var stateLoaded: State.Loaded? = null
        job?.cancel()
        job = viewModelScope.launch {
            try {
                _state.value = State.Loading
                val repo: RepoDetails = repository.getRepository(repoName)
                stateLoaded = State.Loaded(repo, ReadmeState.Loading)
                _state.value = State.Loaded(repo, ReadmeState.Loading)

                getReadme(State.Loaded(repo, ReadmeState.Loading))

            } catch (e: HttpException) {
                handleHttpException(e, stateLoaded)
            } catch (e: IOException) {
                handleNetworkException(stateLoaded)
            } catch (e: Exception) {
                handleOtherException(e, stateLoaded)
            }
        }
    }

    private suspend fun getReadme(state: State.Loaded) {
        val readme: String = repository.getRepositoryReadme(
            ownerName = state.githubRepo.owner,
            repositoryName = state.githubRepo.name,
            branchName = state.githubRepo.defaultBranch
        )
        if (readme.isBlank()) {
            _readmeState.value = ReadmeState.Empty
            _state.value = state.copy(readmeState = ReadmeState.Empty)
        } else {
            _readmeState.value = ReadmeState.Loaded(readme)
            _state.value = state.copy(readmeState = ReadmeState.Loaded(readme))
        }
    }

    private fun handleHttpException(e: HttpException, stateLoaded: State.Loaded?) {
        if (_state.value is State.Loading) {
            _state.value = State.Error(e.message.toString())
            _readmeState.value = ReadmeState.Error(e.message.toString())
        } else {
            if (e.code() == 404) {
                _readmeState.value = ReadmeState.Empty
                updateStateLoaded(stateLoaded = stateLoaded, readmeState = ReadmeState.Empty)
            } else {
                _readmeState.value = ReadmeState.Error(e.message.toString())
                updateStateLoaded(
                    stateLoaded = stateLoaded,
                    readmeState = ReadmeState.Error(e.message.toString())
                )
            }
        }
    }

    private fun handleNetworkException(stateLoaded: State.Loaded?) {
        if (_state.value is State.Loaded) {
            _readmeState.value = ReadmeState.NoInternetError
            updateStateLoaded(stateLoaded = stateLoaded, readmeState = ReadmeState.NoInternetError)
        } else {
            _state.value = State.NoInternetError
            _readmeState.value = ReadmeState.NoInternetError
        }
    }

    private fun handleOtherException(e: Exception, stateLoaded: State.Loaded?) {
        if (_state.value is State.Loaded) {
            _readmeState.value = ReadmeState.Error(e.message.toString())
            updateStateLoaded(
                stateLoaded = stateLoaded,
                readmeState = ReadmeState.Error(e.message.toString())
            )
        } else {
            _state.value = State.Error(e.message.toString())
            _readmeState.value = ReadmeState.Error(e.message.toString())
        }
    }

    private fun updateStateLoaded(stateLoaded: State.Loaded?, readmeState: ReadmeState) {
        if (stateLoaded != null)
            _state.value = stateLoaded.copy(readmeState = readmeState)
    }

    sealed interface State {
        object Loading : State
        object NoInternetError : State
        data class Error(val error: String) : State
        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        object NoInternetError : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    @AssistedFactory
    interface Factory {
        fun create(repoName: String): RepositoryInfoViewModel
    }
}