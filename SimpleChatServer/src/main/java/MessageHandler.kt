import com.google.gson.Gson
import com.google.gson.JsonObject

enum class MessageKinds(val kind: String) {
    SIMPLE_TEXT("SimpleText"),
    NONE("");

    companion object {
        fun fromString(string: String) = values().find { it.kind == string }?:NONE
    }
}

class MessageHandler {
    private val gson = Gson()
    fun handle(jsonStr: JsonObject?): ReceivedMessage {
        return when (val kind = jsonStr?.get("kind")?.asString) {
            "text" -> gson.fromJson(jsonStr, TextMessage::class.java)
            "image" -> gson.fromJson(jsonStr, ImageMessage::class.java)
            else -> UnknownMessage("Unknown kind $kind")
        }.also { it.timeStamp = System.currentTimeMillis() }
    }
}
