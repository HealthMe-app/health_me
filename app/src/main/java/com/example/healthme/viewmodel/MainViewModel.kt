package com.example.healthme.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val user: MutableLiveData<User> = MutableLiveData()

    fun getUser(): Boolean {
        val token = sharedPreferences.getString("token", null)
        return if (token != null) {
            viewModelScope.launch {
                val response = apiRepository.getUser("Token $token")
                user.postValue(response.body())
                myResponse.value = response
            }
            true
        } else
            false
    }

    fun register(
        email: String, first_name: String, sex: Char, date_of_birth: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = apiRepository.register(email, first_name, sex, date_of_birth, password)
            user.postValue(response.body()?.user)
            sharedPreferences.edit().putString("token", response.body()!!.token).apply()
            myResponseUserInfo.value = response
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = apiRepository.login(email, password)
            user.postValue(response.body()?.user)
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
}