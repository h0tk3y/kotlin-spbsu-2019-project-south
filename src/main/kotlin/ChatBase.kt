class ChatBase(val data_path: String = ""): DataBase {
    override var baseSize: Long = 0
    private val chats = mutableMapOf<Long, Chat>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(chat : Chat): Long {
        chat.id = baseSize
        chats.put(baseSize, chat)
        return baseSize++
    }

    fun get(id : Long): Chat? {
        return chats[id]
    }

    fun edit(id : Long, edited_chat: Chat) {
        if (!chats.containsKey(id))
            return
        chats[id] = edited_chat
    }

    fun remove(id: Long) {
        chats.remove(id)
    }

}