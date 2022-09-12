package com.shante.githubrepositoryviewapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

class AuthViewModel {

    val token: MutableLiveData<String> = TODO()
    val state: LiveData<State> = TODO()
    val actions: Flow<Action> = TODO()

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