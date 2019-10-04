class MessageBase {
    private val messages = mutableMapOf<Long, Message>()
    private var baseSize : Int = 0
    fun addMessage(message : Message) {
        messages.plus(Pair(baseSize, message))
        baseSize += 1
    }
    fun getMessage(id : Long): Message? {
        return messages[id]
    }
}