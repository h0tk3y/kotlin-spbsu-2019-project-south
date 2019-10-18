class MessageBase(val data_path: String = "") : DataBase {

    override var baseSize: Long = 0

    private val messages = mutableMapOf<Long, Message>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(message : Message): Long {
        messages.put(baseSize, message)
        return baseSize++
    }

    fun get(id : Long): Message? {
        return messages[id]
    }

    fun edit(id : Long, edited_message: Message) {
        if (!messages.containsKey(id))
            return
        messages[id] = edited_message
    }

    fun remove(id: Long) {
        messages.remove(id)
    }

}