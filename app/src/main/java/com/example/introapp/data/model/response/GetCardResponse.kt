package com.example.introapp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCardResponse(
    @SerialName("profileImg")
    val profileImg: String? = null,

    @SerialName("nickName")
    val nickName: String,

    @SerialName("jobGroup")
    val jobGroup: String,

    @SerialName("techStacks")
    val techStacks: List<String>,

    @SerialName("level")
    val level: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("email")
    val email: String,

    @SerialName("link")
    val link: String
)
