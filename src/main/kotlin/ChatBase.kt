class ChatBase(val data_path: String = ""): DataBase {
    override var baseSize: Int = 0
    private val chats = mutableMapOf<Long, Chat>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

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