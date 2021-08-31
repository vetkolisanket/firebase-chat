package com.sanket.firebasechat.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String
) : Parcelable {
    constructor(): this("", "")         //Needed for creating user object by firebase realtime database
}