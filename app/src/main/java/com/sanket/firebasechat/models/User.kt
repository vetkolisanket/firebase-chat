package com.sanket.firebasechat.models

data class User(
    val id: String,
    val name: String
) {
    constructor(): this("", "")         //Needed for creating user object by firebase realtime database
}