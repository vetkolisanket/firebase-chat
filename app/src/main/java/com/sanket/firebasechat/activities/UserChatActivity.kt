package com.sanket.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanket.firebasechat.R
import com.sanket.firebasechat.databinding.ActivityUserChatBinding
import com.sanket.firebasechat.models.Message
import com.sanket.firebasechat.models.User
import com.sanket.firebasechat.utils.Constants

class UserChatActivity : AppCompatActivity() {

    private val binding: ActivityUserChatBinding by lazy { ActivityUserChatBinding.inflate(layoutInflater) }
    private val user: User by lazy { intent.getParcelableExtra(Constants.BUNDLE_KEYS.USER)!! }
    private val messagesDbRef by lazy {
        FirebaseDatabase.getInstance(Constants.Companion.Api.FIREBASE_DB_REFERENCE)
            .getReference(Constants.Companion.Api.MESSAGES)
    }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        supportActionBar?.title = user.name
        binding.apply {
            btnSend.setOnClickListener {
                if (etChat.text.isNotBlank()) {
                    sendMessage(etChat.text.toString())
                }
            }
        }
    }

    private fun sendMessage(message: String) {
        val msg = Message(message, user.id, auth.uid!!)
        val key = getKey(user.id, auth.uid!!)
        messagesDbRef.child(key).push().setValue(msg)
    }

    private fun getKey(id1: String, id2: String): String {
        if (id1 < id2) {
            return id1 + "_" + id2
        }
        return id2 + "_" + id1
    }
}