/*class Server {
    var chatBase: ChatBase? = null
    var userBase: UserBase? = null
    var messageBase: MessageBase? = null

    fun answerRequest(request : ServerRequest) {}
}


class Client(val server: Server) {
    var loggedUserId: Long = -1 // id залогиненного юзера

    private val objectMapper = jacksonObjectMapper()

    class userDataGetter(val userId: Long)
    class userDataSetter(val userId: Long)
    class messageDataGetter(val messageId: Long)
    class messageDataSetter(val messageId: Long)
    class chatDataGetter(val chatId: Long)
    class chatDataSetter(val chatId: Long)

}


class UI(val client: Client) {

}


class ChatBase {
    private val chats: MutableMap<Long, Chat>? = null // Map(id, chat)
    private var baseSize: Int = 0 //  кол-во чатов
    fun getChat(id: Int) {} // возвращает чат по id
    fun addChat() {} // добавляет чат
}


class UserBase {
    val users: MutableMap<Long, User>? = null // Map(id, chat)
    fun searchByName() {}
    fun searchById() {}
    fun addUser() {}
    fun deleteUser() {}

}


class MessageBase {
    val messages: MutableMap<Long, Message>? = null
    fun getChat(id: Int) {}
}


data class User(val userId: Long, val login: String) {
    var name: String = ""
    val blockedUsers: MutableList<Long> = mutableListOf()
    val userChatsId: List<Long> = mutableListOf()
    val contacts: MutableMap<Long, String> = mutableMapOf() // id, имя контакта
}


data class Message(var text: String, val chatId: Long, val userId: Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false

    //val attachment_ids : MutableList<Long>

    /*val idInChat: Long
    init {
        idInChat = TODO()
    }*/
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
