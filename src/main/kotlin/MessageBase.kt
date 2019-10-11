class MessageBase {
    private val messages = mutableMapOf<Long, Message>()
    private var baseSize : Int = 0
    fun addMessage(message : Message): Int {
        messages.plus(Pair(baseSize, message))
        return baseSize++
    }
    fun getMessage(id : Long): Message? {
        return messages[id]
    }
}