import com.google.gson.Gson
import com.google.gson.JsonObject
import data.ChatMessage

enum class MessageKinds(val kind: String) {
    SIMPLE_TEXT("SimpleText"),
    NONE("");

    companion object {
        fun fromString(string: String) = values().find { it.kind == string }?:NONE
    }
}

class MessageHandler {
    private val gson = Gson()
    fun handle(json: JsonObject?): ReceivedMessage {
        return when (val kind = json?.get("kind")?.asString) {
            "text" -> createText(json)
            "image" -> createImage(json)
            else -> createError(kind)
        }
    }

    private fun createText(json: JsonObject?) = gson.fromJson(json, ReceivedMessage.TextMessage::class.java).also { it.timeStamp = System.currentTimeMillis() }
    private fun createImage(json: JsonObject?) = gson.fromJson(json, ReceivedMessage.ImageMessage::class.java).also { it.timeStamp = System.currentTimeMillis() }
    private fun createError(kind: String?) = ReceivedMessage.UnknownMessage("Unknown kind $kind")
}
