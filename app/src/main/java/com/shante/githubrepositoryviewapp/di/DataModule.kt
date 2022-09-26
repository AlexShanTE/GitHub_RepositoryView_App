package com.shante.githubrepositoryviewapp.di

import android.app.Application
import android.content.Context
import com.shante.githubrepositoryviewapp.data.AppRepositoryImpl
import com.shante.githubrepositoryviewapp.data.RestGitHubApi
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        gitHubApi: RestGitHubApi,
        @ApplicationContext context: Context
    ): AppRepository {
        return AppRepositoryImpl(gitHubApi, context)
    }

}