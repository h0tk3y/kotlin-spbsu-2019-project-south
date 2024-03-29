﻿# Фонд Спидвагона представляет

## Мессенджер SnailMail v0. Документация
*Автор: Андрей К @AndreiKingsley. По всем вопросам пишите в телегу*

----
# Содержание
1. [Содержание](#содержание)
1. [**TODO list**](#todo-list)
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
    1. [ReqType и FieldType](#RequestType-и-FieldType)
    1. [ServerRequest](#serverrequest)
1. [Структура Client](#структура-client)
1. [Авторизация и безопаность](#Авторизация-и-безопаность)
    1. [Авторизация. Класс LOGIN_DATA](#Авторизация.Класс-LOGIN_DATA)
    1. [ACCESS_TOKEN](#ACCESS_TOKEN)


# TODO list

**Обратите внимание, что develop обновляетя! Используйте git pull, что бы обновить её и git merge develop
в вашей ветке, чтобы ваш код корректно работал с текущими реалиями!**


Закончили работать над своей частью - можете помочь товарищу

1. Общее:
    1. Что-то решить с тестами - делать не делать
    1. Разобраться с jwt, безопаностью
    1. Фильтрация данных и переработка работы клент-сервер - ветка data_v1
    1. Разделить чаты на Chat и Dialog.
    1. Добавить базу данных LOGIN_DATA(сам тип и методы на клиенте) - ветка login_data
        

# Глобальная струкутура

## Сервер
Сервер - оболочка над всеми базами данных, работающий с ними. Принимает запросы сервера (TODO), возвращает ответ(JSON - тоже TODO). (По сути - очень глупая коснтрукция - дай мне из такой-то датабазы такую-то строку - сервер даёт)

````kotlin
class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")

    fun answerRequest(stringRequest : String) : String{
        
        val request = ServerRequest(stringRequest)
        
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
class Client() { 
    var loggedUserId: Long = -1

    private val objectMapper = jacksonObjectMapper() 
}
````
// Пример работы клиента см. в [Структура Client](#структура-client)

***Человеческие примеры  работы с запросами есть в презентации(слайды 26 - 27)***


## UI
UI - пользовательский интерфейс, пока что самый простенький консольный


# Основные типы данных

3 типа класса данных, возможно скоро будет 4

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

Chat - скоро будет переделан в GroupChat и SingleChat - классы данных группового чата и диалого соответственно

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
//пока что мапы с JSON-стрингами . Должны иметь конструктор от пути в системе(путь там к SQLQ-файлам)
//и методы get/add/edit/remove вроде понятно что имеется в виду.

# Вспомогательные штуки


Эти типы - "язык", на котором клиент общается с сервером.


## RequestType и FieldType

RequestType - enum, в котором хранятся типы запросов на сервер. 

````kotlin
enum class RequestType{
    ADD{
        override fun toString(): String {
            return "ADD" 
        }
    }, // добавить тело что-то в DB (новый юзер зарегался или новое сообщение). Серверу возвращается id
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
особо понял кста). То есть она сможет быть например http-запросом. TODO: разобраться с тем как рабоатет интернет, чтобы сделать её нормальной. Пока сервер
и клиент живут вместе, можно SR сделать бомжацким.

```kotlin
class ServerRequest {
    var id : Long = -1
    var reqType : RequestType? = null
    var fieldType : FieldType? = null


    private val objectMapper = jacksonObjectMapper() - он будет запаковывать переданные данные в JSON

     constructor (reqType: ReqType? = null, fieldType: FieldType? = null, id: Long = -1, body : String /заjson-неный объект/) {
            this.id = id
            this.reqType = reqType
            this.fieldType = fieldType
        }
    
    constructor(requestString: String) {
         TODO()
    }
    
    override fun toString(): String {
        TODO()
    }
    
    private lateinit var server : Server //  на самом деле адрес
    
    fun makeRequest() = server.answerRequest(this.toString()) // вот тут полная хрень, пока работает если сервер и клиент вместе
        // ну то есть вообще лажа, чтобы её переделать надо понять как рабоатет интернет и 
        // кстати SR ДОЛЖЕН принимать там адерс сервера ну корчое его писать и писать вотэто большое TODO
        // которое требует ещё  каких-то заний кароче лул полный
    
}
```



# Структура Client

class Client было решено разбить на подклассы, отвечающие за то что они делают(предоставляют данные или изменяют их).
    
````kotlin
class Client() {

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

# Авторизация и безопаность

## Авторизация. Класс LOGIN_DATA

Пока что неточно : есть дата класс  LOGIN_DATA, хранящий логин, пароль и id юзера, а также его jwt.
Вот и тут надо поработать с jwt, все дела, как всё сделать более-менее безопасно

## ACCESS_TOKEN

Идея такова: когда серверу приходит запрос, он определюяет по юзеру его уровень доступа к данным.
Например: если он админ чата, то есть доступ ко всему; простой мембер - может ливнуть из него, псмотреть сообщения и т.д. Если он не member чата, то что он тут вообще 
делает. Вот, и в зависимости от этого сервер даёт доступ или нет. Пока что непонятно, как на урове сервера фильтровать запросы(то есть например ему приходит EDIT c чем-то изменнёным
как понять что именно там изменил и разрешено ли ему - пока непонятно)


