package com.sanket.firebasechat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanket.firebasechat.databinding.ItemUserBinding
import com.sanket.firebasechat.models.User
import com.sanket.firebasechat.utils.IItemClickListener

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    private val users = mutableListOf<User>()
    lateinit var itemClickListener: IItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = users.size

    fun add(user: User) {
        users.add(user)
    }

    inner class UserListViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.userContainer.setOnClickListener {
                itemClickListener.onItemClick(
                    users[adapterPosition],
                    binding.userContainer,
                    this,
                    adapterPosition
                )
            }
        }

        fun bind() {
            binding.tvName.text = users[adapterPosition].name
        }
    }
}