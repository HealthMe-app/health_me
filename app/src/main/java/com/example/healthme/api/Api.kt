package com.example.healthme.api

import com.example.healthme.model.Appointment
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // auth methods

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

    // api methods

    @FormUrlEncoded
    @POST("api/appointment/")
    suspend fun addAppointment(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("date_time") date_time: String,
        @Field("comment") comment: String,
        @Field("ptype") ptype: Int
    ): Response<Appointment>

    @FormUrlEncoded
    @POST("api/appointments")
    suspend fun getAppointmentsToDate(
        @Header("Authorization") token: String,
        @Field("date") date: String
    ): Response<List<Appointment>>

    @GET("api/appointments")
    suspend fun getClosetAppointments(@Header("Authorization") token: String)
    : Response<List<Appointment>>

    @GET("api/appointment/{appointmentID}")
    suspend fun getAppointment(
        @Header("Authorization") token: String,
        @Path("appointmentID") id: Int
    ): Response<Appointment>

    @FormUrlEncoded
    @PUT("api/appointment/{appointmentID}")
    suspend fun updateAppointment(
        @Header("Authorization") token: String,
        @Path("appointmentID") id: Int,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("date_time") date_time: String,
        @Field("comment") comment: String,
        @Field("ptype") ptype: Int
    ): Response<Appointment>

    @DELETE("api/appointment/{appointmentID}")
    suspend fun deleteAppointment(
        @Header("Authorization") token: String,
        @Path("appointmentID") id: Int
    ): Response<String>
}