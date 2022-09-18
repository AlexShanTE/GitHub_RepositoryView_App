package com.shante.githubrepositoryviewapp.presentation.detailinfo

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shante.githubrepositoryviewapp.domain.models.RepoDetails
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    private val _readmeState: MutableLiveData<ReadmeState> = MutableLiveData()
    val readmeState: LiveData<ReadmeState> get() = _readmeState

    fun getRepositoryInfo(repositoryId: Int) {
        _state.value = State.Loading
        try {
            viewModelScope.launch {
                val repositoryDetails = repository.getRepository(repositoryId.toString())
                _state.value = State.Loaded(repositoryDetails)
            }
        } catch (error: Throwable) {
            val message = error.message ?: error.toString()
            _state.value = State.Error(message)
        }
    }

    fun getReadMe(ownerName: String, repositoryName: String, branchName: String) {
        _readmeState.value = ReadmeState.Loading
        viewModelScope.launch {
            try {
                val encodedReadMe =
                    repository.getRepositoryReadme(ownerName, repositoryName, branchName)
                val decodedReadMe = Base64.decode(encodedReadMe.content, Base64.DEFAULT)
                val contentReadMe = String(decodedReadMe)
                if (contentReadMe.isBlank()) _readmeState.value = ReadmeState.Empty
                else _readmeState.value = ReadmeState.Loaded(contentReadMe)
            } catch (error: Throwable) {
                val message = error.message ?: error.toString()
                _readmeState.value = ReadmeState.Error(message)
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Loaded(
            val githubRepoDetails: RepoDetails  //todo was val githubRepo: Repo, val readmeState:ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

}