package com.sanket.firebasechat.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T: Activity> Context.openActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}