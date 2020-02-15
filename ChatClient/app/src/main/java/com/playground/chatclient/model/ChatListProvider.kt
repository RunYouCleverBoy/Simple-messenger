package com.playground.chatclient.model

import ReceivedMessage
import TextMessage
import android.content.Context
import android.util.LruCache
import com.google.gson.Gson
import com.snap.android.apis.utils.coroutines.Cor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ChatListProvider(context: Context) {
    private val client = OkHttpClient()
    private val cor = Cor(Cor.singleThreadContext("Cor"))
    private val dbase = ChatListDatabaseProvider(context)
    private val chatData = LruCache<String, ChatData?>(2)

    suspend fun send(text: String) {
        cor.withContext {
            val body = Gson().toJson(TextMessage(text)).toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().url("localhost:1111/chat").post(body).build()
            client.newCall(request).execute()
        }
    }

    suspend fun getChat(chatId: String): ChatData? {
        return chatData[chatId]?:let {
            val chat = cor.withContext{ dbase.database.chatMessagesDao()?.getChatData(chatId) }
            if (chat != null) {
                val messages = cor.withContext { dbase.database.messagesDao()?.readAll(chatId)?.map { TextMessage(it.text, it.ts) } }
                ChatData(chat.chatId, chat.chatLength, messages?.toMutableList()?: mutableListOf(), chat.chatName)
            } else null
        }
    }

    suspend fun reload(chatId: String = STUB_CHAT_ID) {
        cor.withContext {
            
        }
    }

    companion object {
        private const val STUB_CHAT_ID = "TEST_CHAT"
    }

}