package com.shante.githubrepositoryviewapp.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _token: MutableLiveData<String> = MutableLiveData()
    val token: LiveData<String> get() = _token

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: Flow<Action> = _actions.receiveAsFlow()

    init {
        val sharedPrefsToken = repository.getTokenFromSharedPreferences()
        if (!sharedPrefsToken.isNullOrBlank()) {
            setToken(sharedPrefsToken)
            viewModelScope.launch {
                _actions.send(Action.RouteToMain)
            }
        }
    }

    fun onSignButtonPressed() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val user = repository.signIn(token.value.toString()) //todo user??
                repository.saveTokenInSharedPreferences(token.value.toString())
                _actions.send(Action.RouteToMain)
            } catch (error: Throwable) {
                error.printStackTrace()
                val message = error.message ?: error.toString()
                _state.value = State.InvalidInput(message)
                _actions.send(Action.ShowError(message))
            } finally {
                _state.value = State.Idle
            }
        }
    }

    fun setToken(token: String) {
        _token.value = token
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
}