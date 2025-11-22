package com.example.introapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("isSuccess")
    val isSuccess: Boolean,

    @SerialName("code")
    val code: String,

    @SerialName("message")
    val message: String,

    @SerialName("result")
    val result: T
)