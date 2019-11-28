class Stream {
    private val streamData = mutableListOf<ReceivedMessage>()
    val messages get() = streamData.toList()
    fun addMessage(message: ReceivedMessage) {
        streamData.add(message)
    }
}