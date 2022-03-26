package com.example.healthme.api

import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("auth/user/")
    suspend fun getUser(@Header("Authorization") token: String): Response<User>

    @FormUrlEncoded
    @POST("auth/register/")
    suspend fun register(
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("sex") sex: Char,
        @Field("date_of_birth") date_of_birth: String,
        @Field("password") password: String
    ): Response<UserInfo>

    @FormUrlEncoded
    @POST("auth/login/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserInfo>

    @POST("auth/logout/")
    suspend fun logout(@Header("Authorization") token: String): Response<String>
}