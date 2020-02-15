package com.playground.chatclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snap.android.apis.utils.coroutines.launchUI
import kotlinx.android.synthetic.main.chat_window_layout.view.*

class ChatWindowFragment: Fragment() {
    private lateinit var chatListViewModel: ChatListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.chat_window_layout, container, false)
        view.chatList.adapter = ChatListAdapter()
        chatListViewModel = ViewModelProvider(this, ChatListViewModel.Factory(requireContext())).get(ChatListViewModel::class.java)
        view.sendButton.onClickSuspend { chatListViewModel.send(view.newMessageLine.text.toString()); view.newMessageLine.setText("") }
        view.refreshButton.onClickSuspend { chatListViewModel.reload() }
        return view
    }

    private fun View.onClickSuspend(f : suspend (View) -> Unit) {
        setOnClickListener { launchUI{f.invoke(it)} }
    }
}