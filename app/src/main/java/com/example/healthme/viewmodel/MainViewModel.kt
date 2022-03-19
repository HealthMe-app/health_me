package com.example.healthme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import com.example.healthme.repository.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val myResponse: MutableLiveData<Response<User>> = MutableLiveData()
    val myResponseUserInfo: MutableLiveData<Response<UserInfo>> = MutableLiveData()
    val myResponseString: MutableLiveData<Response<String>> = MutableLiveData()

    fun getUser(token: String) {
        viewModelScope.launch {
            val response = apiRepository.getUser(token)
            myResponse.value = response
        }
    }

    fun register(
        email: String, first_name: String, sex: Boolean, date_of_birth: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = apiRepository.register(email, first_name, sex, date_of_birth, password)
            myResponseUserInfo.value = response
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = apiRepository.login(email, password)
            myResponseUserInfo.value = response
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            val response = apiRepository.logout(token)
            myResponseString.value = response
        }
    }
}