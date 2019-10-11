class MessageBase(val data_path: String = "") : DataBase {
    override var baseSize: Int = 0
    private val messages = mutableMapOf<Long, Message>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(message : Message): Int {
        messages.plus(Pair(baseSize, message))
        return baseSize++
    }
    fun get(id : Long): Message? {
        return messages[id]
    }
    fun edit(id : Long, editted_message: Message) {
        if (!messages.containsKey(id))
            return
        messages[id] = editted_message;
    }
    fun remove(id: Long) {
        messages.remove(id)
    }

}