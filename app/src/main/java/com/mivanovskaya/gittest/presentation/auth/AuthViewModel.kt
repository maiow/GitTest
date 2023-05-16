package com.mivanovskaya.gittest.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.domain.AppRepository
import com.mivanovskaya.gittest.presentation.tools.StringValue
import com.mivanovskaya.gittest.presentation.tools.StringValue.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val token: MutableLiveData<String> = MutableLiveData()

    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: Flow<Action> = _actions.receiveAsFlow()

    init {
        token.value = repository.getToken()
        if (!token.value.isNullOrBlank()) {
            viewModelScope.launch {
                _actions.send(Action.RouteToMain)
            }
        }
    }

    private val handler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _actions.send(Action.ShowError("${e.message}"))
        }
        repository.resetToken()
        _state.value = State.InvalidInput(StringResource(R.string.error))
    }

    fun onSignButtonPressed(token: String) {
        if (isNotValid(token)) {
            _state.value = State.InvalidInput(StringResource(R.string.invalid_token))
        } else {
            viewModelScope.launch(handler) {
                _state.value = State.Loading
                repository.saveLogin(repository.signIn(token).login)
                _state.value = State.Idle
                _actions.send(Action.RouteToMain)
            }
        }
    }

    private fun isNotValid(token: String): Boolean = token.contains(Regex("""[ЁёА-я]"""))

    sealed interface State {
        object Idle : State
        object Loading : State
        /** reason не String как в ТЗ, а StringValue, чтобы ViewModel не зависел от context*/
        data class InvalidInput(val reason: StringValue) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }
}
