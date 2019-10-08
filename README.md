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
            if(jsonBody.type == 'User'){
                return  objectMapper.writeValueAsString(userBase.get(request.body.id))
            }
        }
    }
}
````

Клиент - основная машина, принимающая запросы пользователю, обрабатывающая их и отсылающая('умная' машина, которая обрабатывает запросы пользователя до макисмального тупого вида, чтобы скормить его серверу). Паттерн Model-View а как иначе)

````kotlin

````

UI - пользовательский интерфейс, пока что самый простенький консольный




