package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import androidx.lifecycle.LiveData
import com.shante.githubrepositoryviewapp.domain.models.Repo

class RepositoriesListViewModel {

    val state: LiveData<State>  = TODO()

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    // TODO:
}