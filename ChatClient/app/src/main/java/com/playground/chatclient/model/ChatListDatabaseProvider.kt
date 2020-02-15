package com.playground.chatclient.model

import TextMessage
import android.content.Context
import androidx.room.*
import java.io.File

class ChatListDatabaseProvider(context: Context) {
    val database = Room.databaseBuilder(context.applicationContext, Dbase::class.java, File(context.cacheDir, "ChatsDBase").canonicalPath).build()
}

@Database(entities = [TextMessageEntry::class, ChatMetadata::class], version = 1)
abstract class Dbase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDao?
    abstract fun chatMessagesDao() : ChatsMetadataDao?
}

@Dao
interface MessagesDao {
    @Query("SELECT * FROM TextMessages where chatId=:chatId LIMIT :limit OFFSET :offset")
    suspend fun readAll(chatId: String, limit: Int = 100, offset: Int = 0) : List<TextMessageEntry>

    @Insert
    suspend fun insert(textMessage: TextMessage)
}

@Dao
interface ChatsMetadataDao {
    @Query("SELECT * FROM Chats where chatId=:chatId")
    suspend fun getChatData(chatId: String) : ChatMetadata

    @Insert
    suspend fun create(chatId: String, name: String)
}

@Entity(tableName = "Chats", primaryKeys = ["chatId"])
data class ChatMetadata(@ColumnInfo val chatId: String, @ColumnInfo val chatLength: Int, @ColumnInfo val chatName: String)

@Entity(tableName = "TextMessages", primaryKeys = ["messageId", "ts"])
data class TextMessageEntry (
    @ColumnInfo val messageId:String,
    @ColumnInfo val kind: String,
    @ColumnInfo val ts: Long,
    @ColumnInfo val text: String,
    @ColumnInfo val chatId: String
)
