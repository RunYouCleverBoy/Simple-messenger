package com.playground.chatclient.model

import TextMessage
import android.content.Context
import androidx.room.*
import java.io.File

class ChatListDatabaseProvider(context: Context) {
    val database = Room.databaseBuilder(context.applicationContext, Dbase::class.java, File(context.cacheDir, "ChatsDBase").canonicalPath).build()
}

@Database(entities = [TextMessageEntry::class], version = 1)
abstract class Dbase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDao?
}


@Dao
interface MessagesDao {
    @Query("SELECT * FROM TextMessages where messageId=:id and chatId=:chatId LIMIT :limit OFFSET :offset")
    suspend fun readAll(id: String, chatId: String, limit: Int = 100, offset: Int = 0) : List<TextMessageEntry>

    @Insert
    suspend fun insert(textMessage: TextMessage)
}

@Entity(tableName = "TextMessages", primaryKeys = ["messageId", "ts"])
data class TextMessageEntry (
    @ColumnInfo val messageId:String,
    @ColumnInfo val kind: String,
    @ColumnInfo val ts: Long,
    @ColumnInfo val text: String,
    @ColumnInfo val chatId: String
)
