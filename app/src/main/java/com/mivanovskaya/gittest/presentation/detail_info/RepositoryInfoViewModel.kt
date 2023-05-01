package com.mivanovskaya.gittest.presentation.detail_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.data.AppRepository
import com.mivanovskaya.gittest.domain.model.RepoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

//    init {
//        getRepoInfo()
//    }

    fun passArgument(repoId: String) = getRepoInfo(repoId)

    fun getRepoInfo(repoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = State.Loading
                //val repo = repository.getRepository(repoId)
                _state.value = State.Loaded(repository.getRepository(repoId),
                    //repository.getReadme(repoId))
                    ReadmeState.Loading)
            } catch (e: Exception) {
                _state.value = State.Error(e.message.toString())
            }
        }
    }

    fun onLogout() {
        repository.logout()
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val githubRepo: RepoDetails,
            //val githubRepo: Repo, - в ТЗ указано так, но также указано,
            // что в AppRepository приходит RepoDetails
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    // TODO:
}