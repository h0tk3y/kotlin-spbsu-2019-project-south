data class Message(var text : String, val chatId : Long, val userId : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false

    val attachment_ids : MutableList<Long>

    val idInChat: Long
    init {
        idInChat = TODO()
    }
}
