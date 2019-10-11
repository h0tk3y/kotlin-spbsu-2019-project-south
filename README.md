# Фонд Спидвагона представляет

## Мессенджер SnailMail v0. Документация
*Автор: Андрей К @AndreiKingsley. По всем вопросам пишите в телегу*

----
# Содержание
1. [Содержание](#содержание)
1. [**ИНФА ДЛЯ ДЕВЕЛОПЕРОВ**](#инфа-для-девелоперов)
1. [Глобальная структура](#глобальная-струкутура)
    1. [Сервер](#сервер)
    1. [Клиент](#клиент)
    1. [UI](#ui)
1. [Основные типы данных](#основные-типы-данных)
    1. [User](#user)
    1. [Message](#message)
    1. [Chat](#chat)
    1. [Базы данных](#базы-данных)
1. [Вспомогательные штуки](#вспомогательные-штуки)
    1. [ReqType и FieldType](#reqtype--fieldtype)
    1. [ServerRequest](#serverrequest)
1. [Структура Client](#структура-client)


# ИНФА ДЛЯ ДЕВЕЛОПЕРОВ

Кто-что делает:

1. ВСЕ ДЕЛАЮТ ТЕСТЫ!

1. Андрей К. - отдыхает потому что потратил всю молодость чтобы написать
эту долбанную документацию и отвечает на ваши вопросы, параллельно разираясь с сервером Ktor(и хочет что
бы ему в этом помогали).

2. Иван П. - переписывает свои старые DB-шки под новую сигнатуру запросов(ну то есть геттеры, сеттеры ремув по id) и
(по его желанию) вникает в работу с человескими DB в Котлине. 

3. Даня А. - пишет обработчик запросов на сервере

Кто-то из нас там ещё депенденсис делает и всё прочее.

4. Никита Б. / Глеб О. / Илья В. - пишут(доделлывают до ума(придумывают все необходимые поля геттеры и сеттеры))
классам данных  User / Message / Chat и делают соответсвующие методы подклассов Client (всё низкоуровненове API я написал
, осталось просто обработать всевеозможные задачи, сами их придумайте пожвлуйста, прмиеры я привёл). 

5. Кто сделал своё и хочет помочь - сделать человеческую сигнатуру(отредактровать на 
основе этой документации, работы своей и других людей),
перевести документацию с моего языка на русский,
нужно разобраться с Ktor, запросами(ServerRequest), подлключить Jakson,
умничка если кто-то разберётся с REST API в Ktor https://ktor.io/quickstart/guides/api.html.
+ я не особо понимаю как сделать чтобы сервер на одном компе работал и к нему подулючиться с другого

# Глобальная струкутура

## Сервер
Сервер - оболочка над всеми базами данных, работающий с ними. Принимает запросы сервера (TODO), возвращает ответ(JSON - тоже TODO). (По сути - очень глупая коснтрукция - дай мне из такой-то датабазы такую-то строку - сервер даёт)

````kotlin
class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")

    fun answerRequest(stringRequest : String) : String{
        
        val request = ServerRequest(stringRequest)
        
        // тут бы написать что-то по типу swith case а не миллион ифоф
        if(request.reqType == GET){
            val jsonBody = readValue<RequestData>(request.body)
            if(request.fieldType == USER){
                return  objectMapper.writeValueAsString(userBase.get(request.body.id))
            }
        }
    }
}
````

## Клиент

Клиент - основная машина, принимающая запросы пользователю, обрабатывающая их и отсылающая их на сервер('умная' машина, которая обрабатывает запросы пользователя до макисмального тупого вида, чтобы скормить его серверу). Паттерн Model-View а как иначе)
Дочерние классы клиента разделяют работу по типу, и имеют методы, отправляяющие запросы на сервер

````kotlin
class Client(private val server: Server) { //Client знает interface Server 
    var loggedUserId: Long = -1

    private val objectMapper = jacksonObjectMapper() 
}
````
// Пример работы клиента см. в [Структура Client](#структура-client)

***Человеческие примеры  работы с запросами есть в презентации(слайды 26 - 27)***


## UI
UI - пользовательский интерфейс, пока что самый простенький консольный


# Основные типы данных

3 типа класса данных, у всего должны быть геттеры

## User

Класс данных с полями пользователя 

````kotlin
data class User(val userId: Long, val login: String) {
    var name: String = ""
    val blockedUsers: MutableList<Long> = mutableListOf()
    val userChatsId: List<Long> = mutableListOf()
    val contacts: MutableMap<Long, String> = mutableMapOf() // id, имя контакта
}
````

## Message

Класс данных с полями сообщения

````kotlin
data class Message(var text : String, val chatId : Long, val userId : Long) {
    var isEdited: Boolean = false
    var isRead: Boolean = false
    var isSent: Boolean = false
    var isDeleted: Boolean = false
}
````

## Chat

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

## Базы данных

Классы, соответсвующие базам данных - обёртки над 'человечкскими'(например SQL-елевскими). Имеют методы для IO, реализуемые пакетами для работы с выбранными DB(например https://habr.com/ru/post/414483/).
//пока что мапы с JSON-стрингами

# Вспомогательные штуки


## ReqType и FieldType

ReqType - enum, в котором хранятся типы запросов на сервер. 

````kotlin
enum class ReqType{
    ADD{
        override fun toString(): String {
            return "ADD" 
        }
    }, // добавить тело что-то в DB (новый юзер зарегался или нвоое сообщение)
    GET, // достать инфу по id
    EDIT, // изменить что-то с данным id
    REMOVE // удалить что-то с данным id
}
````

FieldType - enum, в котором хранится наши осноыне типы данных. 


````kotlin
enum class FieldType{
    USER{
        override fun toString(): String {
            return "USER" 
        }
    }, 
    MESSAGE,
    CHAT
}
````

## ServerRequest

Структура данных, которая хранит все поля запроса. Я хочу разобраться с запросами и сделать её нормальной,
пока что она делает умеет превращать себя в подобие человеческого запроса со стрингом и обратно(тут-с надо разораться с паттернами я там не 
особо понял кста).

```kotlin
class ServerRequest(reqType : ReqType = null, fieldType: FieldType = null, val id: Long = -1, val smth : data class /* TODO */){
    //тут должен быть конструктор от стринга

    private val objectMapper = jacksonObjectMapper() - он будет запаковывать переданные данные в JSON

    fun toString(){
        TODO()
    }   

    fun makeRequest = server.answerRequest(this.toString())
    
}
```



# Структура Client

class Client было решено разбить на подклассы, отвечающие за то что они делают(предоставляют данные или изменяют их).
    
````kotlin
class Client(val server: Server) {

    class userData(val userId: Long = -1){
           // коммент для того кто это будет реализовывать можно добавить шабло makeRequest для функция типа (GET, USER)
           // вот я хз как но сто проц можно и было бы очень классно

       fun addContact(val contactId: Long){
            val cur = objectMapper.readValue<User>(ServerRequest(GET, USER, userId).makeRequest())
            cur.contacts.add(contactId)
            ServerRequest(EDIT, USER, userId, cur).makeRequest()
        }
    }

    class messageData(val messageId: Long)
    class chatData(val chatId: Long)

}
````



