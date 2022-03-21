package com.example.healthme.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthme.repository.ApiRepository

class MainViewModelFactory(
    private val sharedPreferences: SharedPreferences,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(sharedPreferences, apiRepository) as T
    }
}