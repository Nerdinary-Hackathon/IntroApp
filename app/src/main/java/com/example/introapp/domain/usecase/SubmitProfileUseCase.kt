package com.example.introapp.domain.usecase

import com.example.introapp.domain.entity.Profile
import com.example.introapp.domain.entity.User
import com.example.introapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SubmitProfileUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(profile: Profile): Flow<Result<User>> = flow {
        emit(userRepository.submitProfile(profile))
    }
}