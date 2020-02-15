package data

import org.litote.kmongo.Id

data class Chat(val _id: Id<Chat>, val chatName: String)
data class ChatMessage(val _id: Id<ChatMessage>, val chatId: Id<Chat>, val chatText: String, val timeStamp: Long)