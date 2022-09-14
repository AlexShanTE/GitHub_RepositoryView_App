package com.shante.githubrepositoryviewapp.di

import android.app.Application
import com.shante.githubrepositoryviewapp.data.AppRepositoryImpl
import com.shante.githubrepositoryviewapp.data.RestGitHubApi
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        gitHubApi: RestGitHubApi,
        application: Application
    ): AppRepository {
        return AppRepositoryImpl(gitHubApi, application)
    }

}