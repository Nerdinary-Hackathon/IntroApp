package com.example.introapp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCardListResponse(
    @SerialName("content")
    val content: List<CardItem>,

    @SerialName("pageSize")
    val pageSize: Int,

    @SerialName("nextCursor")
    val nextCursor: String? = null,

    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class CardItem(
    @SerialName("profileImg")
    val profileImg: String,

    @SerialName("nickName")
    val nickName: String,

    @SerialName("jobGroup")
    val jobGroup: String,
)