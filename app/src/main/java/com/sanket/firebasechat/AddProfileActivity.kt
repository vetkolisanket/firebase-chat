package com.sanket.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanket.firebasechat.databinding.ActivityAddProfileBinding

class AddProfileActivity : AppCompatActivity() {

    private val binding: ActivityAddProfileBinding by lazy {
        ActivityAddProfileBinding.inflate(
            layoutInflater
        )
    }
    private val usersDbRef by lazy {
        FirebaseDatabase.getInstance("https://fir-chat-d008d-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
    }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_submit -> {
                val name = binding.etName.text.toString().trim()
                if (name.isNotBlank()) {
                    auth.uid?.let { uid ->
                        usersDbRef.child(uid).setValue(User(name))
                        Toast.makeText(this, "Name added to db", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}