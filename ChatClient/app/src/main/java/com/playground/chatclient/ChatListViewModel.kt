package com.playground.chatclient

import TextMessage
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.playground.chatclient.model.ChatListProvider
import com.snap.android.apis.utils.coroutines.Cor
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request


class ChatListViewModel(context: Context) : ViewModel() {
    private val provider = ChatListProvider(context)

    suspend fun send(message: String) = provider.send(message)
    suspend fun reload() = provider.reload()

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Context::class.java).newInstance(context)
        }

    }

    override fun onCleared() {
        super.onCleared()
    }
}