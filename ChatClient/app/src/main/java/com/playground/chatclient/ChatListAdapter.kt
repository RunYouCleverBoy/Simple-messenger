package com.playground.chatclient

import ImageMessage
import ReceivedMessage
import TextMessage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ChatListAdapter : ListAdapter<ReceivedMessage, ChatViewHolder>(object: DiffUtil.ItemCallback<ReceivedMessage>() {
    override fun areItemsTheSame(oldItem: ReceivedMessage, newItem: ReceivedMessage): Boolean = oldItem == newItem && oldItem.kind == newItem.kind
    override fun areContentsTheSame(oldItem: ReceivedMessage, newItem: ReceivedMessage): Boolean = oldItem.timeStamp == newItem.timeStamp && oldItem.kind == newItem.kind
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return TextChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.one_line, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TextMessage -> 1
            is ImageMessage -> 2
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TextMessage -> (holder as? TextChatViewHolder)?.text?.text = item.text
        }
    }
}
