package com.shante.githubrepositoryviewapp.presentation

import androidx.lifecycle.ViewModel
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun onLogOutClicked() {
        repository.removeTokenFromSharedPreferences()
    }

}