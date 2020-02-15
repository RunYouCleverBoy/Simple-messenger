package com.playground.chatclient.model

import ReceivedMessage

data class ChatData(val chatId: String, val overallLength: Int, val items:MutableList<out ReceivedMessage>, val name: String)