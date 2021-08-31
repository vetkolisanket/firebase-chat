package com.sanket.firebasechat.models

data class Message(

    val message: String,

    val fromId: String,

    val toId: String,

    val timestamp: Long = System.currentTimeMillis()

) {
}