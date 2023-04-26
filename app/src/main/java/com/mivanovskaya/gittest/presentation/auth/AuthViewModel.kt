package com.mivanovskaya.gittest.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.data.AppRepository
import com.mivanovskaya.gittest.data.KeyValueStorage
import com.mivanovskaya.gittest.data.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository,
    private val keyValueStorage: KeyValueStorage
) : ViewModel() {

    // val token: MutableLiveData<String>
    // val state: LiveData<State>
    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<Action>()
    val actions = _actions.asSharedFlow()

    private val handler = CoroutineExceptionHandler { _, e ->
        Log.i("BRED", "message is: ${e.message} & error is: $e")
        _state.value = State.InvalidInput(e.message.toString())
        //_actions.emit(Action.ShowError("${e.message}"))
        keyValueStorage.authTokenEnabled = false
        Log.i("BRED", "VM: KVS TknEn = ${keyValueStorage.authTokenEnabled}")
    }

    fun onSignButtonPressed(token: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _state.value = State.Loading
            Log.i("BRED", "VM: token is $token")
            repository.signIn(token)
            _actions.emit(Action.RouteToMain)
            //go to next screen with args?
            //_state.value = State.Idle
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
