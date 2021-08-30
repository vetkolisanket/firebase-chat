package com.sanket.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sanket.firebasechat.R
import com.sanket.firebasechat.adapters.UserListAdapter
import com.sanket.firebasechat.databinding.ActivityUserListBinding
import com.sanket.firebasechat.models.User
import com.sanket.firebasechat.utils.Constants
import com.sanket.firebasechat.utils.IItemClickListener
import com.sanket.firebasechat.utils.openActivity

class UserListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUserListBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val usersDbRef by lazy {
        FirebaseDatabase.getInstance(Constants.Companion.Api.FIREBASE_DB_REFERENCE)
            .getReference(Constants.Companion.Api.USERS)
    }
    private val adapter by lazy { UserListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        fetchUsers()
    }

    private fun initViews() {
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logoutUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        auth.signOut()
        openActivity<SplashActivity>()
        finish()
    }

    private fun fetchUsers() {
        usersDbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // TODO: 29/08/21 Fetch all users from fb realtime db and add at once
                snapshot.children.forEach { childSnapShot ->
                    val user: User? = childSnapShot.getValue(User::class.java)
                    user?.let {
                        if (auth.uid != user.id) {
                            adapter.add(it)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                // TODO: 29/08/21 hide progress
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: 29/08/21 hide progress
            }

        })
    }

    private fun initRecyclerView() {
        adapter.itemClickListener = object : IItemClickListener {
            override fun onItemClick(
                item: Any,
                view: View,
                viewHolder: RecyclerView.ViewHolder,
                position: Int,
                bundle: Bundle?
            ) {
                when (view.id) {
                    R.id.userContainer -> openActivity<UserChatActivity>()
                }
            }

        }
        binding.rvUsers.apply {
            this.adapter = this@UserListActivity.adapter
            layoutManager = LinearLayoutManager(this@UserListActivity)
            // TODO: 29/08/21 Add item decoration
        }
    }
}