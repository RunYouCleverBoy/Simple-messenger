import data.Chat
import data.ChatMessage
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.*

class MongoWrapper {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    suspend fun chats(): List<Chat> = database.getCollection("Chats", Chat::class.java).find().toList()
    suspend fun readChat(chatId: Id<Chat>) : List<ChatMessage> = database.getCollection("ChatMessages", ChatMessage::class.java).find(ChatMessage::chatId eq chatId).toList()
    suspend fun addChat(chat: Chat) = database.getCollection("Chats", Chat::class.java).insertOne(chat)
    suspend fun addMessage(chatMessage: ChatMessage) = database.getCollection("ChatMessages", ChatMessage::class.java).insertOne(chatMessage)
}