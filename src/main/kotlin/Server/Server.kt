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

    private fun processRequest(request : ServerRequest) : ServerResponse {
        val response = ServerResponse(request.requestType)
        val requestHandler = RequestHandler()
        when (request.requestType) {
            TransportType.GET_USER -> {
                val user : User? = requestHandler.getUser(request.id)
                response.body = objectMapper.writeValueAsString(user)
            }
            TransportType.SEND_MESSAGE -> requestHandler.sendMessage(request.body)
            TransportType.GET_CHAT -> {
                val chat : Chat? = requestHandler.getChat(request.id)
                response.body = objectMapper.writeValueAsString(chat)
            }
            TransportType.GET_MESSAGES -> {
                val messages : List<Message> = requestHandler.getMessages(request.id, request.body)
                response.body = objectMapper.writeValueAsString(messages)
            }
            TransportType.REGISTER -> TODO()
            TransportType.LOGIN -> TODO()
            TransportType.DELETE_MESSAGE -> requestHandler.deleteMessage(request.id)
            TransportType.DELETE_CHAT -> requestHandler.deleteChat(request.id)
            TransportType.EDIT_MESSAGE -> requestHandler.editMessage(request.id, request.body)
            TransportType.EDIT_USER -> requestHandler.editUser(request.id, request.body)
            TransportType.GET_USER_CHATS -> {
                val chats : List<Chat> = requestHandler.getUserChats(request.id)
                response.body = objectMapper.writeValueAsString(chats)
            }
            TransportType.ADD_CHAT -> requestHandler.addChat(request.body)
            TransportType.REMOVE_USER -> requestHandler.removeUser(request.id, request.body)
            TransportType.EDIT_CHAT -> requestHandler.editChat(request.id, request.body)
            TransportType.ADD_MEMBER -> requestHandler.addMember(request.id, request.body)
            TransportType.ADD_ADMIN -> requestHandler.addAdmin(request.id, request.body)
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

