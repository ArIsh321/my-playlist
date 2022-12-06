package com.test.myplaylist.di.module

import com.test.myplaylist.domain.repository.Repository
import com.test.myplaylist.domain.repository.RepositoryImpl
import com.test.myplaylist.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)

}
