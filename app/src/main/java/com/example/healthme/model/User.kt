package com.example.healthme.model

data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val sex: Char,
    val date_of_birth: String,
    val pic: String
)
