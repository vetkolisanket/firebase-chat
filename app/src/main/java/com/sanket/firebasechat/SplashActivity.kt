package com.sanket.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanket.firebasechat.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val usersDbRef by lazy {
        FirebaseDatabase.getInstance(Constants.Companion.Api.FIREBASE_DB_REFERENCE)
            .getReference(Constants.Companion.Api.USERS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        redirectToAppropriatePage()
    }

    private fun redirectToAppropriatePage() {
        if (auth.currentUser == null) {
            openActivity<LoginActivity>()
            finish()
        } else {
            usersDbRef.child(auth.uid!!).get().addOnSuccessListener {
                if (it.exists()) {
                    openActivity<UserListActivity>()
                } else {
                    openActivity<AddProfileActivity>()
                }
                finish()
            }
        }
    }
}