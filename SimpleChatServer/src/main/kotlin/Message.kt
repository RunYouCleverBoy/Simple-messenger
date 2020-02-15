import com.google.gson.annotations.SerializedName

sealed class ReceivedMessage {
    open class TextMessage(
        @SerializedName("text") val text: String,
        @SerializedName("kind") val kind: String = "text",
        @SerializedName("ts") var timeStamp: Long = System.currentTimeMillis()
    ) : ReceivedMessage()

    open class ImageMessage(
        @SerializedName("imageUri") val uri: String,
        @SerializedName("title") val title: String,
        @SerializedName("kind") val kind: String = "image",
        @SerializedName("ts") var timeStamp: Long = System.currentTimeMillis()
    ) : ReceivedMessage()

    class UnknownMessage(
        @SerializedName("errorKind") val errorKind: String,
        @SerializedName("kind") val kind: String = "error",
        @SerializedName("ts") var timeStamp: Long = System.currentTimeMillis()
    ) : ReceivedMessage()
}