data class Message(var text : String = "", var id : Long = -1, val chatId : Long = -1, val userId : Long = -1) {
    var edited: Boolean = false
    var read: Boolean = false
    var sent: Boolean = false
    var deleted: Boolean = false
}