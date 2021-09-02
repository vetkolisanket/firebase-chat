package com.sanket.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sanket.firebasechat.R
import com.sanket.firebasechat.adapters.MessageListAdapter
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
    private val adapter by lazy { MessageListAdapter(user) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        getMessages()
    }

    private fun getMessages() {
        val key = getKey(user.id, auth.uid!!)
        messagesDbRef.child(key).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { childSnapShot ->
                    val message = childSnapShot.getValue(Message::class.java)
                    message?.let {
                        adapter.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: 01/09/21 hide progress
            }
        })
    }

    private fun initViews() {
        supportActionBar?.title = user.name
        binding.apply {
            btnSend.setOnClickListener {
                if (etChat.text.isNotBlank()) {
                    sendMessage(etChat.text.toString())
                }
            }
            rvMessages.apply {
                layoutManager = LinearLayoutManager(this@UserChatActivity)
                adapter = this@UserChatActivity.adapter
            }
        }
    }

    private fun sendMessage(message: String) {
        val msg = Message(message, auth.uid!!, user.id)
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