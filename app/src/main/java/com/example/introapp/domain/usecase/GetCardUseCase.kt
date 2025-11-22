package com.example.introapp.domain.usecase

import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCardUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Result<Card>> = flow {
        if (userId.isBlank()) {
            emit(Result.failure(IllegalArgumentException("사용자 ID가 비어있습니다")))
            return@flow
        }

        emit(userRepository.getCard(userId))
    }
}