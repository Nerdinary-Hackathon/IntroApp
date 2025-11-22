package com.example.introapp.domain.usecase

import com.example.introapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 명함 교환 UseCase
 */
class ExchangeCardUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String, cardCode: String): Flow<Result<Unit>> = flow {
        if (cardCode.isBlank()) {
            emit(Result.failure(IllegalArgumentException("명함 코드가 비어있습니다")))
            return@flow
        }

        emit(userRepository.exchangeCard(userId, cardCode))
    }
}