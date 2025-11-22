package com.example.introapp.domain.entity

/**
 * 명함 목록 페이징 도메인 엔티티
 */
data class CardList(
    val cards: List<CardSummary>,
    val pageSize: Int,
    val nextCursor: String?,
    val hasNext: Boolean
)