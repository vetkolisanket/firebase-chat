package com.sanket.firebasechat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanket.firebasechat.R
import com.sanket.firebasechat.databinding.ItemCurrentUsersMessageBinding
import com.sanket.firebasechat.models.Message
import com.sanket.firebasechat.utils.Constants
import com.sanket.firebasechat.utils.DateUtils

class MessageListAdapter: RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder>() {

    private val messages = mutableListOf<Message>()

    inner class MessageListViewHolder(val binding: ItemCurrentUsersMessageBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val binding = ItemCurrentUsersMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = messages.size

    fun add(message: Message) {
        messages.add(message)
    }

}