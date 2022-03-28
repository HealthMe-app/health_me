package com.example.healthme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Appointment(
    val id: Int,
    val ptype: AppointmentType,
    val user: User,
    val name: String,
    val address: String,
    val date_time: String,
    var comment: String
): Parcelable
