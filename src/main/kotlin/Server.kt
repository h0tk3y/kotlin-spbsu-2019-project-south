import com.fasterxml.jackson.module.kotlin.*
import io.ktor.application.install
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket

object Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")


    private fun processRequest(request: ServerRequest) : ServerResponse {
        val response = ServerResponse(request.requestType, request.fieldType)
        when (request.fieldType) {
            FieldType.USER -> {
                val user: User = objectMapper.readValue(request.body)
                when (request.requestType) {
                    TransportType.GET -> response.body = objectMapper.writeValueAsString(userBase.get(user.id))
                    TransportType.ADD -> response.body = objectMapper.writeValueAsString(userBase.add(user))
                    TransportType.REMOVE -> userBase.remove(user.id)
                    TransportType.EDIT -> userBase.edit(user.id, user)
                    TransportType.FIND -> response.body = objectMapper.writeValueAsString(userBase.find(user.id))
                }
            }
            FieldType.CHAT -> {
                val chat: Chat = objectMapper.readValue(request.body)
                when (request.requestType) {
                    TransportType.GET -> response.body = objectMapper.writeValueAsString(chatBase.get(chat.id))
                    TransportType.ADD -> chatBase.add(chat)
                    TransportType.REMOVE -> chatBase.remove(chat.id)
                    TransportType.EDIT -> chatBase.edit(chat.id, chat)
                    TransportType.FIND -> response.body = objectMapper.writeValueAsString(chatBase.find(chat.id))
                }
            }
            FieldType.MESSAGE -> {
                val message: Message = objectMapper.readValue(request.body)
                when (request.requestType) {
                    TransportType.GET -> response.body = objectMapper.writeValueAsString(messageBase.get(message.id))
                    TransportType.ADD -> messageBase.add(message)
                    TransportType.REMOVE -> messageBase.remove(message.id)
                    TransportType.EDIT -> messageBase.edit(message.id, message)
                    TransportType.FIND -> response.body = objectMapper.writeValueAsString(messageBase.find(message.id))
                }
            }
        }
        return response
    }

    fun run(port : Int = 9999) {
        embeddedServer(Netty, port) {
            install(WebSockets)
            routing {
                webSocket("/") {
                    while (true) {
                        val frame = incoming.receive()
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val objectMapper = jacksonObjectMapper();
                            val request : ServerRequest? = objectMapper.readValue(text)
                            if (request != null) {
                                val response = processRequest(request)
                                outgoing.send(Frame.Text(objectMapper.writeValueAsString(response)))
                            }
                        }
                    }
                }
            }
        }.start(wait = true)
    }
}

