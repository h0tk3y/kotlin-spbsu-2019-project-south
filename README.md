# Фонд Спидвагона 

## Мессенджер SnailMail v0

----
### Глобальная струкутура

#### Сервер
Сервер - оболочка над всеми базами данных, работающий с ними. Принимает запросы сервера (TODO), возвращает ответ(JSON - тоже TODO). (По сути - очень глупая коснтрукция - дай мне из такой-то датабазы такую-то строку - сервер даёт)

````kotlin
class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")

    fun answerRequest(request : ServerRequest) : String{
        if(request.type == 'GET'){
            val jsonBody = readValue<RequestData>(request.body)
            if(jsonBody.type == 'USER'){
                return  objectMapper.writeValueAsString(userBase.get(request.body.id))
            }
        }
    }
}
````

#### Клиент

Клиент - основная машина, принимающая запросы пользователю, обрабатывающая их и отсылающая их на сервер('умная' машина, которая обрабатывает запросы пользователя до макисмального тупого вида, чтобы скормить его серверу). Паттерн Model-View а как иначе)
Дочерние классы клиента разделяют работу по типу, и имеют методы, отправляяющие запросы на сервер

````kotlin
class Client(private val server: Server) {
    var loggedUserId: Long = -1

    private val objectMapper = jacksonObjectMapper()

    class userDataGetter(val userId: Long){
        fun getUserLogin() : String{
            val json = server.answerRequest(ServerRequest('GET', 'USER', userId))
            return objectMapper.readValue<UserData>(json).login
        }   
    }   
}
````

***Человеческие примеры  работы с запросами есть в презентации(слайды 26 - 27)***


#### UI
UI - пользовательский интерфейс, пока что самый простенький консольный


### Основные типы данных

3 типа класса данных, у всего должны быть геттеры

#### User

Класс данных с полями пользователя 

````kotlin
data class User(val userId: Long, val login: String) {
    var name: String = ""
    val blockedUsers: MutableList<Long> = mutableListOf()
    val userChatsId: List<Long> = mutableListOf()
    val contacts: MutableMap<Long, String> = mutableMapOf() // id, имя контакта
}
````

#### Message

Класс данных с полями сообщения

````kotlin
data class Message(var text : String, val chatId : Long, val userId : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}
````

#### Chat

Chat - стандартный interface, от которого наследуются GroupChat и SingleChat - классы данных группового чата и диалого соответственно

````kotlin
interface Chat {
    var members: MutableSet<Long>
    val messages: MutableList<Message>
    val id: Long
}

data class GroupChat(override val id: Long, val chatOwner: Long, var Name: String) : Chat {
    override var members: MutableSet<Long> = mutableSetOf(chatOwner)
    override val messages: MutableList<Message> = mutableListOf()
}


data class singleChat(override val id: Long, val user1: Long, val user2: Long) : Chat {
    override var members: MutableSet<Long> = mutableSetOf(user1, user2)
    override val messages: MutableList<Message> = mutableListOf()
}
````



