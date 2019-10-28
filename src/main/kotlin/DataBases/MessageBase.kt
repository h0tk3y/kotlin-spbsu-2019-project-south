class MessageBase(val dataPath: String = "") : DataBase {

    override var baseSize: Long = 0

    private val messages = mutableMapOf<Long, Message>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(message : Message): Long {
        message.id = baseSize
        messages.put(baseSize, message)
        return baseSize++
    }

    fun get(id : Long): Message? {
        return messages[id]
    }

    fun edit(id : Long, editedMessage: Message) {
        if (!messages.containsKey(id))
            return
        messages[id] = editedMessage
    }

    fun remove(id: Long) {
        messages.remove(id)
    }

}