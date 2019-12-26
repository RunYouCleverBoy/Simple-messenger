package com.playground.chatclient

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.one_line.view.*

sealed class ChatViewHolder(view: View): RecyclerView.ViewHolder(view)
class TextChatViewHolder(view: View) : ChatViewHolder(view) {
    val text = view.chatLineText
}

