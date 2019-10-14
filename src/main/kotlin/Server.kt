import com.fasterxml.jackson.module.kotlin.*
import io.ktor.application.install
import io.ktor.features.AutoHeadResponse.install
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket

class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")

    private fun processRequest(request: ServerRequest) : String {
        when (request.fieldType) {
            FieldType.USER -> {
                val user: User = objectMapper.readValue(request.body)
                when (request.requestType) {
                    RequestType.GET -> return objectMapper.writeValueAsString(userBase.get(user.id))
                    RequestType.ADD -> userBase.add(user)
                    RequestType.REMOVE -> userBase.remove(user.id)
                    RequestType.EDIT -> userBase.edit(user.id, user)
                }
            }
            FieldType.CHAT -> {
                val chat: Chat = objectMapper.readValue(request.body)
                when (request.requestType) {
                    RequestType.GET -> return objectMapper.writeValueAsString(chatBase.get(chat.id))
                    RequestType.ADD -> chatBase.add(chat)
                    RequestType.REMOVE -> chatBase.remove(chat.id)
                    RequestType.EDIT -> chatBase.edit(chat.id, chat)
                }
            }
            FieldType.MESSAGE -> {
                val message: Message = objectMapper.readValue(request.body)
                when (request.requestType) {
                    RequestType.GET -> return objectMapper.writeValueAsString(messageBase.get(message.id))
                    RequestType.ADD -> messageBase.add(message)
                    RequestType.REMOVE -> messageBase.remove(message.id)
                    RequestType.EDIT -> messageBase.edit(message.id, message)
                }
            }
        }
        return ""
    }

    fun respondToRequest(port : Int = 9999) {
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
                                outgoing.send(Frame.Text(response))
                            }
                        }
                    }
                }
            }
        }.start(wait = true)
    }
}

