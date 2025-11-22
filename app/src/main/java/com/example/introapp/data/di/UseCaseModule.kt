package com.example.introapp.data.di

import com.example.introapp.domain.repository.UserRepository
import com.example.introapp.domain.usecase.ExchangeCardUseCase
import com.example.introapp.domain.usecase.GetCardListUseCase
import com.example.introapp.domain.usecase.GetCardUseCase
import com.example.introapp.domain.usecase.SubmitProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSubmitProfileUseCase(repository: UserRepository): SubmitProfileUseCase =
        SubmitProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideExchangeCardUseCase(repository: UserRepository): ExchangeCardUseCase =
        ExchangeCardUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCardUseCase(repository: UserRepository): GetCardUseCase =
        GetCardUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCardListUseCase(repository: UserRepository): GetCardListUseCase =
        GetCardListUseCase(repository)

}