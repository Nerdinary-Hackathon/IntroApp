package com.example.introapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class EmptyResponse(
    // kotlinx.serialization이 완전히 빈 클래스를 지원하지 않아서 더미 필드 추가
    val dummy: String? = null
)
