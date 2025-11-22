package com.example.introapp.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(
    @SerialName("name")
    val name: String,

    @SerialName("nickname")
    val nickname: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("email")
    val email: String,

    @SerialName("link")
    val link: String,

    @SerialName("jobGroup")
    val jobGroup: String,

    @SerialName("level")
    val level: String,

    @SerialName("techStackNames")
    val techStackNames: List<String>,
)