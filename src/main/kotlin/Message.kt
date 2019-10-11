data class Message(var text : String, val id : Long, val chatId : Long, val userId : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}