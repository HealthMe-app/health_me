package com.example.healthme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteType(
    val id: Int,
    val name: String,
    val icon: String,
    val icon_pink: String,
    val icon_birch: String
): Parcelable
