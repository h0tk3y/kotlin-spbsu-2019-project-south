class ChatBase(val dataPath: String = ""): DataBase {
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

    fun edit(id : Long, editedChat: Chat) {
        if (!chats.containsKey(id))
            return
        chats[id] = editedChat
    }

    fun remove(id: Long) {
        chats.remove(id)
    }

}