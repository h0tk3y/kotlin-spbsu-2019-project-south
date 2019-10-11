/*class Server {
    var chatBase : ChatBase
    var userBase : UserBase
    var attachmentBase : AttachmentBase
    var messageBase : MessageBase
}

class Client {}

class ChatBase {
    val chats : MutableMap<Long, Chat>
        get() {
            TODO()
        }

    fun search() {}
    fun addChat() {}
}

class UserBase {
    val users : MutableMap<Long, User>? = null
    fun searchByName() {}
    fun searchByMainl() {}
    fun addUser() {}
    fun deleteUser() {}

}

class MessageBase {
    val messages : MutableMap<Long, Message> = null
}

class User {

    val blockedUsers : List<Long>
        get() {
            TODO()
        }

    val userChatsId : List<Long>
        get() {
            TODO()
        }

    lateinit var name : String
    val contacts : Map<Long, String>
        get() {
            TODO()
        }
    fun createChat() {}
    fun addChat() {}
    fun removeChat() {}
    fun sendMessage() {}
    fun deleteMessage() {}
    fun findMessage () {}
    fun leaveChat() {}
}

class AttachmentBase {
    val Attachments : MutableMap<Long, Attachment>
        get() {
            TODO()
        }

    fun Download() {}
}

class Attachment {
    val type : String = ""
}

class MessageBase {

}

class Message {
    val idInChat : Long
    var isEdited : Boolean = false
    var isRead : Boolean = false
    var isSent : Boolean = false
    var isDeleted : Boolean = false
    val messageAttachments : List<Attachment>
    var text : String
    val autherId : Long
}

interface Chat {
    var isPrivate : Boolean
    val Messages : List<Message>
    val Id : Long
    fun SendNotification() {}
    fun Attach() {}
}

class GroupChat : Chat {
    var Members : MutableMap<User>
    val chatOwner : User
    var Name : String
    var Reference : String
    fun AddMember() {}
    fun DeleteMember() {}
}

class singleChat : Chat {
    val user1 : User
    val user2 : User
}*/

