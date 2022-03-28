package com.example.healthme.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthme.model.Appointment
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import com.example.healthme.repository.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    private val sharedPreferences: SharedPreferences,
    private val apiRepository: ApiRepository
) : ViewModel() {

    val myResponse: MutableLiveData<Response<User>> = MutableLiveData()
    val myResponseUserInfo: MutableLiveData<Response<UserInfo>> = MutableLiveData()
    val myResponseString: MutableLiveData<Response<String>> = MutableLiveData()

    val myResponseAppointment: MutableLiveData<Response<Appointment>> = MutableLiveData()
    val myResponseAppointments: MutableLiveData<Response<List<Appointment>>> = MutableLiveData()
    val myResponseClosetAppointments: MutableLiveData<Response<List<Appointment>>> =
        MutableLiveData()
//    val user: MutableLiveData<User> = MutableLiveData()

    // auth methods

    fun getUser(): Boolean {
        val token = sharedPreferences.getString("token", null)
        return if (token != null) {
            viewModelScope.launch {
                val response = apiRepository.getUser("Token $token")
//                user.postValue(response.body())
                myResponse.value = response
            }
            true
        } else
            false
    }

    fun register(
        email: String,
        first_name: String,
        sex: Char,
        date_of_birth: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = apiRepository.register(email, first_name, sex, date_of_birth, password)
//            user.postValue(response.body()?.user)
            sharedPreferences.edit().putString("token", response.body()!!.token).apply()
            myResponseUserInfo.value = response
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = apiRepository.login(email, password)
//            user.postValue(response.body()?.user)
            sharedPreferences.edit().putString("token", response.body()!!.token).apply()
            myResponseUserInfo.value = response
        }
    }

    fun logout() {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.logout("Token $token")
            myResponseString.value = response
            sharedPreferences.edit().clear().apply()
        }
    }

    // api appointment methods

    fun addAppointment(
        name: String,
        address: String,
        date_time: String,
        comment: String,
        ptype: Int
    ) {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.addAppointment(
                "Token $token",
                name,
                address,
                date_time,
                comment,
                ptype
            )
            myResponseAppointment.value = response
        }
    }

    fun getAppointmentsToDate(date: String) {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.getAppointmentsToDate("Token $token", date)
            myResponseAppointments.value = response
        }
    }

    fun getClosetAppointments() {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.getClosetAppointments("Token $token")
            myResponseClosetAppointments.value = response
        }
    }

    fun getAppointment(id: Int) {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.getAppointment("Token $token", id)
            myResponseAppointment.value = response
        }
    }

    fun updateAppointment(
        id: Int,
        name: String,
        address: String,
        date_time: String,
        comment: String,
        ptype: Int
    ) {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.updateAppointment(
                "Token $token",
                id,
                name,
                address,
                date_time,
                comment,
                ptype
            )
            myResponseAppointment.value = response
        }
    }

    fun deleteAppointment(id: Int) {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.deleteAppointment("Token $token", id)
            myResponseString.value = response
        }
    }
}