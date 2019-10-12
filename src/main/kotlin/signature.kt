/*import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

enum class RequestType {
    ADD {
        override fun toString(): String {
            return "ADD"
        }
    },
    GET,
    EDIT,
    REMOVE
}

enum class FieldType {
    USER {
        override fun toString(): String {
            return "USER"
        }
    },
    MESSAGE,
    CHAT
}

class ServerRequest {
    var id: Long = -1
    var requestType: RequestType? = null
    var fieldType: FieldType? = null

    private val objectMapper = jacksonObjectMapper()

    constructor (requestType: RequestType? = null, fieldType: FieldType? = null, id: Long = -1, body: String) }

    constructor(requestString: String)

    override fun toString(): String

    private lateinit var server: Server //  на самом деле адрес

    fun makeRequest() = server.answerRequest(this.toString())

}

class Server {
    var chatBase: ChatBase? = null
    var userBase: UserBase? = null
    var messageBase: MessageBase? = null

    fun answerRequest(request: String) {}
}


class Client() {
    var loggedUserId: Long = -1

    private val objectMapper = jacksonObjectMapper()

    class userData(val userId: Long)
    class messageData(val messageId: Long)
    class chatData(val chatId: Long)
}



class UI(val client: Client) {

}



class ChatBase: DataBase {
    override var baseSize: Int = 0
    private val chats = mutableMapOf<Long, Chat>()
    fun add(chat : Chat): Int
    fun get(id : Long): Chat?
    fun edit(id : Long, editted_chat: Chat)
    fun remove(id: Long)
}



class UserBase: DataBase {
    override var baseSize: Int = 0
    private val users = mutableMapOf<Long, User>()
    fun add(user : User): Int
    fun get(id : Long): User?
    fun edit(id : Long, editted_user: User)
    fun remove(id: Long)
}



class MessageBase : DataBase {
    override var baseSize: Int = 0
    private val messages = mutableMapOf<Long, Message>()
    fun add(message : Message): Int
    fun get(id : Long): Message?
    fun edit(id : Long, editted_message: Message)
    fun remove(id: Long)
}



data class User(val userId: Long, val login: String) {
    var name: String = ""
    val blockedUsers: MutableList<Long> = mutableListOf()
    val userChatsId: List<Long> = mutableListOf()
    val contacts: MutableMap<Long, String> = mutableMapOf() // id, имя контакта
}



data class Message(var text : String, val id : Long, val chatId : Long, val userId : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}



interface Chat {
    var members: MutableSet<Long>
    val messages: MutableList<Message>
    val id: Long

    //var isPrivate : Boolean
}



data class GroupChat(override val id: Long, val chatOwner: Long, var Name: String) : Chat {

    override var members: MutableSet<Long> = mutableSetOf(chatOwner)
    override val messages: MutableList<Message> = mutableListOf()

    //var Reference : String
}



data class singleChat(override val id: Long, val user1: Long, val user2: Long) : Chat {
    override var members: MutableSet<Long> = mutableSetOf(user1, user2)
    override val messages: MutableList<Message> = mutableListOf()
}
*/