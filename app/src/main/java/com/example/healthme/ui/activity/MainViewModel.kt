package com.example.healthme.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthme.model.User
import com.example.healthme.model.UserInfo
import com.example.healthme.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<User>> = MutableLiveData()
    val myResponseUserInfo: MutableLiveData<Response<UserInfo>> = MutableLiveData()
    val myResponseString: MutableLiveData<Response<String>> = MutableLiveData()

    fun getUser(token: String) {
        viewModelScope.launch {
            val response = repository.getUser(token)
            myResponse.value = response
        }
    }

    fun register(
        email: String, first_name: String, sex: Boolean, date_of_birth: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = repository.register(email, first_name, sex, date_of_birth, password)
            myResponseUserInfo.value = response
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(email, password)
            myResponseUserInfo.value = response
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            val response = repository.logout(token)
            myResponseString.value = response
        }
    }
}