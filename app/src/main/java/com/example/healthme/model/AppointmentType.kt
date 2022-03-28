package com.example.healthme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppointmentType(
    val id: Int,
    val name: String,
    val icon: String,
): Parcelable
