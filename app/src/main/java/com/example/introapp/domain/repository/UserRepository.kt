package com.example.introapp.domain.repository

import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.CardList
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.entity.Profile
import com.example.introapp.domain.entity.User

/**
 * 사용자 도메인 Repository 인터페이스
 */
interface UserRepository {

    /**
     * 프로필 제출
     */
    suspend fun submitProfile(profile: Profile): Result<User>

    /**
     * 명함 교환
     */
    suspend fun exchangeCard(userId: String, cardCode: String): Result<Unit>

    /**
     * 명함 조회
     */
    suspend fun getCard(userId: String): Result<Card>

    /**
     * 명함 목록 조회
     */
    suspend fun getCardList(
        userId: String,
        cursor: String?,
        size: Int = 10,
        jobGroup: JobGroup
    ): Result<CardList>
}