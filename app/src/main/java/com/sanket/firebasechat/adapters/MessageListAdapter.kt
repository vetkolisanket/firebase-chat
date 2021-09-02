package com.sanket.firebasechat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanket.firebasechat.R
import com.sanket.firebasechat.databinding.ItemCurrentUsersMessageBinding
import com.sanket.firebasechat.databinding.ItemOtherUsersMessageBinding
import com.sanket.firebasechat.models.Message
import com.sanket.firebasechat.models.User
import com.sanket.firebasechat.utils.Constants
import com.sanket.firebasechat.utils.DateUtils

class MessageListAdapter(val otherUser: User): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val CURRENT_USER = 0
        const val OTHER_USER = 1
    }

    private val messages = mutableListOf<Message>()

    inner class UsersMessageViewHolder(private val binding: ItemCurrentUsersMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val message = messages[adapterPosition]
            binding.apply {
                tvName.text = tvName.context.getString(R.string.you)
                tvMessage.text = message.message
                tvTime.text = DateUtils.getDate(message.timestamp, Constants.DATE_FORMAT)
            }
        }
    }

    inner class OthersMessageViewHolder(private val binding: ItemOtherUsersMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val message = messages[adapterPosition]
            binding.apply {
                tvName.text = otherUser.name
                tvMessage.text = message.message
                tvTime.text = DateUtils.getDate(message.timestamp, Constants.DATE_FORMAT)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (CURRENT_USER == viewType) {
            val binding = ItemCurrentUsersMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            UsersMessageViewHolder(binding)
        } else {
            val binding = ItemOtherUsersMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OthersMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (CURRENT_USER == getItemViewType(position)) {
            (holder as UsersMessageViewHolder).bind()
        } else {
            (holder as OthersMessageViewHolder).bind()
        }

    }

    override fun getItemCount() = messages.size

    fun add(message: Message) {
        messages.add(message)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.fromId == otherUser.id) {
            return OTHER_USER
        }
        return CURRENT_USER
    }

}