import java.util.*

class HtmlRenderer {
    fun render(stream: Stream): String {
        val tags = stream.messages.map (this::tagMapper)
        return "<html><body>${tags.joinToString("\n")}</body></html>"
    }

    private fun tagMapper(message: ReceivedMessage):String {
        return when (message) {
            is TextMessage -> "<div>${Date(message.timeStamp)}<p>${message.text}</p></div>"
            is ImageMessage -> "<img src=\"${message.uri}\">${message.title}</img>"
            else -> "---"
        }
    }
}