package com.example.healthme.model

import androidx.room.ColumnInfo

data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val sex: Boolean,
    val date_of_birth: String
)
