package com.mivanovskaya.gittest.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    // val token: MutableLiveData<String>
    // val state: LiveData<State>
    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    // val actions: Flow<Action>

    private val handler = CoroutineExceptionHandler { _, e ->
        Log.i("BRED", "${e.message} $e")
        _state.value = State.InvalidInput(e.message.toString())
    }

    fun onSignButtonPressed(token: String) {
        viewModelScope.launch(handler) {
            _state.value = State.Loading
            repository.signIn(token)
            _state.value = State.Idle
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }

// TODO:

}
