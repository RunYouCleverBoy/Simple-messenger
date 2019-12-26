import com.google.gson.annotations.SerializedName

open class ReceivedMessage(@SerializedName("kind") val kind: String, @SerializedName("ts") var timeStamp:Long = System.currentTimeMillis())
open class TextMessage(@SerializedName("text") val text: String) : ReceivedMessage(kind = "text")
open class ImageMessage(@SerializedName("imageUri" ) val uri: String, @SerializedName("Title") val title:String) : ReceivedMessage(kind = "image")

class UnknownMessage(val errorKind:String): ReceivedMessage(kind = "ERROR")