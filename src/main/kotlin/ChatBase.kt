class ChatBase: DataBase {
    override var baseSize: Int = 0
    private val chats = mutableMapOf<Long, Chat>()
    fun add(chat : Chat): Int {
        chats.plus(Pair(baseSize, chat))
        return baseSize++
    }
    fun get(id : Long): Chat? {
        return chats[id]
    }
    fun edit(id : Long, editted_chat: Chat) {
        if (!chats.containsKey(id))
            return
        chats[id] = editted_chat;
    }
    fun remove(id: Long) {
        chats.remove(id)
    }
}