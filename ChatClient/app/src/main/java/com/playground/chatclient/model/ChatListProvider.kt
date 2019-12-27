package com.playground.chatclient.model

import TextMessage
import android.content.Context
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
    suspend fun send(text: String) {
        cor.withContext {
            val body = Gson().toJson(TextMessage(text)).toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().url("localhost:1111/chat").post(body).build()
            client.newCall(request).execute()
        }
    }

    suspend fun reload() {

    }

}