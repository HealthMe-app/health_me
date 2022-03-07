package com.example.healthme.repository

import com.example.healthme.api.RetrofitInstance
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import retrofit2.Response

class Repository {

    suspend fun getUser(token: String): Response<User> {
        return RetrofitInstance.api.getUser(token)
    }

    suspend fun register(
        email: String, first_name: String, sex: Boolean, date_of_birth: String,
        password: String
    ): Response<UserInfo> {
        return RetrofitInstance.api.register(email, first_name, sex, date_of_birth, password)
    }

    suspend fun login(email: String, password: String): Response<UserInfo> {
        return RetrofitInstance.api.login(email, password)
    }

    suspend fun logout(token: String): Response<String> {
        return RetrofitInstance.api.logout(token)
    }
}