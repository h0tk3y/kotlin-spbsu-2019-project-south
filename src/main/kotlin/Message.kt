data class Message(var text : String, val chatId : Long, val userId : Long, val idInChat : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}