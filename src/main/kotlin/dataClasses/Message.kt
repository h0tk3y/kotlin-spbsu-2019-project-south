data class Message(var text : String = "", var id : Long = -1, val chatId : Long = -1, val userId : Long = -1) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}