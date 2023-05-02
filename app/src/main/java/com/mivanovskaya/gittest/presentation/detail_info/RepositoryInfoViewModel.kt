package com.mivanovskaya.gittest.presentation.detail_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.data.AppRepository
import com.mivanovskaya.gittest.domain.model.RepoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _readmeState = MutableStateFlow<ReadmeState>(ReadmeState.Loading)
    val readmeState = _readmeState.asStateFlow()

//    init {
//        getRepoInfo()
//    }

    fun onGettingArgument(repoId: String) = getRepoInfo(repoId)

    fun onLogoutButtonPressed() = repository.logout()

    private fun getRepoInfo(repoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = State.Loading
                val repo = repository.getRepository(repoId)
                Log.i("BRED", "VM sent request to api on Repo Details")
                _state.value = State.Loaded(repo, ReadmeState.Loading)
                delay(2000)
                Log.i("BRED", "VM branch: ${repo.default_branch}")
                val readme = repository.getRepositoryReadme(
                    ownerName = repo.login,
                    repositoryName = repoId,
                    branchName = repo.default_branch
                )
                Log.i("BRED", "VM sent request to api on Repo Readme, which is $readme")
                if (readme.isBlank())
                    _readmeState.value = ReadmeState.Empty
                else _readmeState.value = ReadmeState.Loaded(readme)
            } catch (e: Exception) {
                if ((e is HttpException)) {
                    _readmeState.value = ReadmeState.Empty
                    Log.i("BRED1", e.message.toString())
                } else {
                    _state.value = State.Error(e.message.toString())
                    Log.i("BRED2", e.message.toString())
                    _readmeState.value = ReadmeState.Error(e.message.toString())
                }
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }
}