package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import com.shante.githubrepositoryviewapp.presentation.auth.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: Flow<Action> = _actions.receiveAsFlow()

    fun getRepositories(): List<Repo> {
        var repoList: List<Repo> = emptyList()
        _state.value = State.Loading
        viewModelScope.launch {
            try {
                repoList = repository.getRepositories()
                _state.value = State.Loaded(repoList)
                if (repoList.isEmpty()) {
                    _state.value = State.Empty
                }
            } catch (error: Throwable) {
                val message = error.message ?: error.toString()
                _state.value = State.Error(message)
                _actions.send(Action.ShowError(message))
            }
        }
        return repoList
    }


    fun onRepositoryCardClicked(repository: Repo) {
        viewModelScope.launch {
            _actions.send(Action.RouteToDetails(repository.id))
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        data class RouteToDetails(val repositoryId: Int) : Action
    }


}