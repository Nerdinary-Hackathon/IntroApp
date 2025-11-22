package com.example.introapp.domain.usecase

import com.example.introapp.domain.entity.CardList
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCardListUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        userId: String,
        cursor: String? = null,
        size: Int = DEFAULT_PAGE_SIZE,
        jobGroup: JobGroup
    ): Flow<Result<CardList>> = flow {
        if (userId.isBlank()) {
            emit(Result.failure(IllegalArgumentException("사용자 ID가 비어있습니다")))
            return@flow
        }

        if (size !in MIN_PAGE_SIZE..MAX_PAGE_SIZE) {
            emit(Result.failure(
                IllegalArgumentException("페이지 크기는 $MIN_PAGE_SIZE~$MAX_PAGE_SIZE 사이여야 합니다")
            ))
            return@flow
        }

        emit(userRepository.getCardList(userId, cursor, size, jobGroup))
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MIN_PAGE_SIZE = 1
        private const val MAX_PAGE_SIZE = 100
    }
}