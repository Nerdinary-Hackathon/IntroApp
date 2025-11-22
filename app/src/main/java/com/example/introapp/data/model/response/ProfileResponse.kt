package com.example.introapp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("userId")
    val userId: Int,
)
