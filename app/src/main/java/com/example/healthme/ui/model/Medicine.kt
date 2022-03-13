package com.example.healthme.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val indication: String,
    val contraIndication: String,
    val sideEffects: String,
    val dosage: String
)