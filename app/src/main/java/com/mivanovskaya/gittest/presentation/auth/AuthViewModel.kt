package com.mivanovskaya.gittest.presentation.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

): ViewModel() {

   // val token: MutableLiveData<String>
   // val state: LiveData<State>
   // val actions: Flow<Action>

    fun onSignButtonPressed() {
        // TODO:
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
