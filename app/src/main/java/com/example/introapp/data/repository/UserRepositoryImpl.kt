package com.example.introapp.data.repository

import com.example.introapp.data.api.UserService
import com.example.introapp.data.util.safeApiCall
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.CardList
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.entity.Profile
import com.example.introapp.domain.entity.User
import com.example.introapp.domain.mapper.UserMapper.toEntity
import com.example.introapp.domain.mapper.UserMapper.toRequest
import com.example.introapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * UserRepository 구현체
 */
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun submitProfile(profile: Profile): Result<User> {
        return safeApiCall(
            apiCall = { userService.submitProfile(profile.toRequest()) },
            transform = { it.toEntity() }
        )
    }

    override suspend fun exchangeCard(userId: String, cardCode: String): Result<Unit> {
        return safeApiCall {
            userService.exchangeCard(userId, cardCode)
        }.map { Unit } // EmptyResponse를 Unit으로 변환
    }

    override suspend fun getCard(userId: String): Result<Card> {
        return safeApiCall(
            apiCall = { userService.getCard(userId) },
            transform = { it.toEntity() }
        )
    }

    override suspend fun getCardList(
        userId: String,
        cursor: String?,
        size: Int,
        jobGroup: JobGroup
    ): Result<CardList> {
        return safeApiCall(
            apiCall = { userService.getCardList(userId, cursor ?: "", size, jobGroup.value) },
            transform = { it.toEntity() }
        )
    }
}