
//Сервер, хранит все базы данных, принимает запросы клиента и отвечает на них

class Server {

    // эти датабазы - оболочки над человческими

    var chatBase : ChatBase? = null
    var userBase : UserBase? = null
    // var attachmentBase : AttachmentBase = null
    var messageBase : MessageBase? = null

    fun request(){} // URL -> JSON
}

// Клиент, обменивается ифнормацией с сервером, отвечает за UI-шку

class Client(val server: Server) {
    var loggedUserId : Long = -1 // id залогиненного юзера

    //тут должен быть чел, которые делает запрос на сервер и который парсит ответы на них

    //все функции - запросы вида клиент -> сервер -> клиент

    fun login() {}// авторизация
    fun register(){} // регистрация
}


//
// пока что консольный с дефолтными командами, пока не реализоывваем
class UI(val client: Client){

}

// база даннных сначала испоользуем мапы, в дальнейшем перейдём на номральные DB. Сервер будет уметь с ними работать
// и по запросу, например юзера, берёт ифну из DB и возварщает JSON

class ChatBase {
    private val chats : MutableMap<Long, Chat>? = null // Map(id, chat)
    private var baseSize : Int = 0 //  кол-во чатов
    fun getChat(id: Int) {} // возвращает чат по id
    fun addChat() {} // добавляет чат
}

class UserBase {
    val users : MutableMap<Long, User>? = null // Map(id, chat)
    fun searchByName() {}
    fun searchById() {}
    fun addUser() {}
    fun deleteUser() {}

}

class MessageBase {
    val messages : MutableMap<Long, Message>? = null
    fun getChat(id: Int) {}
}

// User - основной класс, в котором хранятся все его данные, а также имеет все необходимые методы
// Основной конструктор - по JSON - у

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

//пока забить
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


class Message {
    val idInChat : Long = -1
    var isEdited : Boolean = false
    var isRead : Boolean = false
    var isSent : Boolean = false
    var isDeleted : Boolean = false
    // val messageAttachments : List<Attachment>
    var text : String = ""
    val autherId : Long = -1
}

interface Chat {
    //var isPrivate : Boolean
    val Messages : List<Message>
    val Id : Long
    //fun SendNotification() {}
    //fun Attach() {}
}

/*
class GroupChat : Chat {
    var Members : MutableMap<User>
    val chatOwner : User
    var Name : String
    var Reference : String
    fun AddMember() {}
    fun DeleteMember() {}
}
*/


//Пофиксите пожалуйста что тут он ругается
class singleChat : Chat {
    val user1 : User
    val user2 : User
}

