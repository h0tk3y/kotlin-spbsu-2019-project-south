import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.*
import io.ktor.application.install
import io.ktor.features.AutoHeadResponse.install
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.net.http.HttpClient

class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")

    private fun processRequest(request: ServerRequest) : String {
        when (request.fieldType) {
            FieldType.USER -> {
                val user: User = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(userBase.get(user.id))
                    ReqType.ADD -> userBase.add(user)
                    ReqType.REMOVE -> userBase.remove(user.id)
                    ReqType.EDIT -> userBase.edit(user.id, user)
                }
            }
            FieldType.CHAT -> {
                val chat: Chat = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(chatBase.get(chat.id))
                    ReqType.ADD -> chatBase.add(chat)
                    ReqType.REMOVE -> chatBase.remove(chat.id)
                    ReqType.EDIT -> chatBase.edit(chat.id, chat)
                }
            }
            FieldType.MESSAGE -> {
                val message: Message = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(messageBase.get(message.id))
                    ReqType.ADD -> messageBase.add(message)
                    ReqType.REMOVE -> messageBase.remove(message.id)
                    ReqType.EDIT -> messageBase.edit(message.id, message)
                }
            }
        }
        return ""
    }

    fun respondToRequest(port : Int = 9999) {
        embeddedServer(Netty, port) {
            install(WebSockets)
            routing {
                webSocket {
                    while (true) {
                        val frame = incoming.receive()
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val request : ServerRequest? = ServerRequest(text)
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

