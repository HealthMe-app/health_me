package com.example.healthme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id: Int,
    val ntype: NoteType,
    val user: User,
    val name: String,
    val date_time: String,
    var comment: String
): Parcelable
