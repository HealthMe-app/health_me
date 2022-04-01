package com.example.healthme.repository

import com.example.healthme.api.RetrofitInstance
import com.example.healthme.model.Appointment
import com.example.healthme.model.Note
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import java.util.*

class ApiRepository {

    // auth methods

    suspend fun getUser(token: String): Response<User> {
        return RetrofitInstance.api.getUser(token)
    }

    suspend fun register(
        email: String,
        first_name: String,
        sex: Char,
        date_of_birth: String,
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

    // api appointment methods

    suspend fun addAppointment(
        token: String,
        name: String,
        address: String,
        date_time: String,
        comment: String,
        ptype: Int
    ): Response<Appointment> {
        return RetrofitInstance.api.addAppointment(token, name, address, date_time, comment, ptype)
    }

    suspend fun getAppointmentsToDate(token: String, date: String): Response<List<Appointment>> {
        return RetrofitInstance.api.getAppointmentsToDate(token, date)
    }

    suspend fun getClosetAppointments(token: String): Response<List<Appointment>> {
        return RetrofitInstance.api.getClosetAppointments(token)
    }

    suspend fun getAppointment(token: String, id: Int): Response<Appointment> {
        return RetrofitInstance.api.getAppointment(token, id)
    }

    suspend fun updateAppointment(
        token: String,
        id: Int,
        name: String,
        address: String,
        date_time: String,
        comment: String,
        ptype: Int
    ): Response<Appointment> {
        return RetrofitInstance.api.updateAppointment(token, id, name, address, date_time, comment, ptype)
    }

    suspend fun deleteAppointment(token: String, id: Int): Response<String> {
        return RetrofitInstance.api.deleteAppointment(token, id)
    }

    // api appointment methods

    suspend fun addNote(
        token: String,
        ntype: Int,
        name: String,
        date_time: String,
        comment: String
    ): Response<Note> {
        return RetrofitInstance.api.addNote(token, ntype, name, date_time, comment)
    }

    suspend fun getNotesToDate(token: String, date: String): Response<List<Note>> {
        return RetrofitInstance.api.getNotesToDate(token, date)
    }

    suspend fun getNote(token: String, id: Int): Response<Note> {
        return RetrofitInstance.api.getNote(token, id)
    }

    suspend fun updateNote(
        token: String,
        id: Int,
        ntype: Int,
        name: String,
        date_time: String,
        comment: String
    ): Response<Note> {
        return RetrofitInstance.api.updateNote(token, id, ntype, name, date_time, comment)
    }

    suspend fun deleteNote(token: String, id: Int): Response<String> {
        return RetrofitInstance.api.deleteNote(token, id)
    }
}