class ChatBase {
    private val chats = mutableMapOf<Long, Chat>()
    private var baseSize : Int = 0
    fun addChat(chat : Chat): Int {
        chats.plus(Pair(baseSize, chat))
        return baseSize++
    }
    fun getChat(id : Long): Chat? {
        return chats[id]
    }
}