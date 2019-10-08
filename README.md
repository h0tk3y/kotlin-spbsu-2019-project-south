# Фонд Спидвагона 

## Мессенджер SnailMail v0

----
### Глобальная струкутура
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

Клиент - основная машина, принимающая запросы пользователю, обрабатывающая их и отсылающая('умная' машина, которая обрабатывает запросы пользователя до макисмального тупого вида, чтобы скормить его серверу). Паттерн Model-View а как иначе)

````kotlin
class Client(private val server: Server) {
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

UI - пользовательский интерфейс, пока что самый простенький консольный


### Основные типы данных




