class ChatBase {
    private val chats = mutableMapOf<Long, Message>()
    private var baseSize : Int = 0
    fun addChat(chat : Chat) {
        chats.plus(Pair(baseSize, chat))
        baseSize += 1
    }
    fun getChat(id : Long): Message? {
        return chats[id]
    }
}