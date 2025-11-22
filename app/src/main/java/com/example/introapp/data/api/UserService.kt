package com.example.introapp.data.api

import com.example.introapp.data.model.ApiResponse
import com.example.introapp.data.model.ApiResponseEmpty
import com.example.introapp.data.model.request.ProfileRequest
import com.example.introapp.data.model.response.EmptyResponse
import com.example.introapp.data.model.response.GetCardListResponse
import com.example.introapp.data.model.response.GetCardResponse
import com.example.introapp.data.model.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    /**
     * 프로필 작성
     */
    @POST("users/profile")
    suspend fun submitProfile(
        @Body profileRequest: ProfileRequest
    ): ApiResponse<ProfileResponse>

    /**
     * 명함 교환
     */
    @POST("users/exchange-card")
    suspend fun exchangeCard(
        @Header("userId") userId: String,
        @Query("cardCode") cardCode: String,
    ): ApiResponseEmpty

    /**
     * 명함 조회
     */
    @GET("users/{user-Id}")
    suspend fun getCard(
        @Path("user-Id") userId: String,
    ): ApiResponse<GetCardResponse>

    /**
     * 명함 목록 조회
     */
    @GET("users/cards")
    suspend fun getCardList(
        @Header("userId") userId: String,
        @Query("cursor") cursor: String,
        @Query("size") size: Int = 10,
        @Query("jobGroup") jobGroup: String, // PM, DESIGNER, WEB, BACKEND, ANDROID, IOS
    ): ApiResponse<GetCardListResponse>

}