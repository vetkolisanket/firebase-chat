package com.sanket.firebasechat.utils

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface IItemClickListener {
    fun onItemClick(item: Any, view: View, viewHolder: RecyclerView.ViewHolder, position: Int, bundle: Bundle? = null)
}