data class Message(var text : String, val chatId : Long) {

    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false

    val idInChat: Long

    init {
        idInChat = TODO()
    }
}