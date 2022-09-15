package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    val state: LiveData<State> = TODO()

    fun getRepositories(): List<Repo> {
        TODO()
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    // TODO:
}