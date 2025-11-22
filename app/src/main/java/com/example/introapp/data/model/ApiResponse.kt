package com.example.introapp.data.model

import com.example.introapp.data.model.response.EmptyResponse
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

@Serializable
data class ApiResponseEmpty(
    @SerialName("isSuccess")
    val isSuccess: Boolean,

    @SerialName("code")
    val code: String,

    @SerialName("message")
    val message: String,

    @SerialName("result")
    val result: EmptyResponse = EmptyResponse()
)