package com.example.introapp.domain.mapper

import com.example.introapp.data.model.request.ProfileRequest
import com.example.introapp.data.model.response.CardItem
import com.example.introapp.data.model.response.GetCardListResponse
import com.example.introapp.data.model.response.GetCardResponse
import com.example.introapp.data.model.response.ProfileResponse
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.CardList
import com.example.introapp.domain.entity.CardSummary
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.entity.Level
import com.example.introapp.domain.entity.Profile
import com.example.introapp.domain.entity.TechStack
import com.example.introapp.domain.entity.User

/**
 * Data Layer ↔ Domain Layer 변환 Mapper
 */
object UserMapper {

    // ========== Response → Entity ==========

    /**
     * ProfileResponse → User
     */
    fun ProfileResponse.toEntity(): User {
        return User(userId = this.userId)
    }

    /**
     * GetCardResponse → Card
     */
    fun GetCardResponse.toEntity(): Card {
        return Card(
            profileImg = this.profileImg,
            nickname = this.nickName,
            jobGroup = JobGroup.from(this.jobGroup),
            techStacks = this.techStacks.map { TechStack.from(it) },
            level = Level.from(this.level),
            phoneNumber = this.phoneNumber,
            email = this.email,
            link = this.link
        )
    }

    /**
     * GetCardListResponse → CardList
     */
    fun GetCardListResponse.toEntity(): CardList {
        return CardList(
            cards = this.content.map { it.toEntity() },
            pageSize = this.pageSize,
            nextCursor = this.nextCursor,
            hasNext = this.hasNext
        )
    }

    /**
     * CardItem → CardSummary
     */
    private fun CardItem.toEntity(): CardSummary {
        return CardSummary(
            profileImg = this.profileImg,
            nickname = this.nickName,
            jobGroup = JobGroup.from(this.jobGroup)
        )
    }

    // ========== Entity → Request ==========

    /**
     * Profile → ProfileRequest
     */
    fun Profile.toRequest(): ProfileRequest {
        return ProfileRequest(
            name = this.name,
            nickname = this.nickname,
            phone = this.phone,
            email = this.email,
            link = this.link,
            jobGroup = this.jobGroup.value,
            level = this.level.value,
            techStackNames = this.techStacks.map { it.value }
        )
    }
}