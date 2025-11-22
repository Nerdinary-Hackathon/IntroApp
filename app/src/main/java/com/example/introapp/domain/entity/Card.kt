package com.example.introapp.domain.entity

/**
 * 명함 상세 정보 도메인 엔티티
 */
data class Card(
    val profileImg: String,
    val nickname: String,
    val jobGroup: JobGroup,
    val techStacks: List<TechStack>,
    val level: Level,
    val phoneNumber: String,
    val email: String,
    val link: String
)

/**
 * 명함 간단 정보 (목록용)
 */
data class CardSummary(
    val profileImg: String,
    val nickname: String,
    val jobGroup: JobGroup
)