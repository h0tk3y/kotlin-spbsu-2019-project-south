import Transport.ResponseStatus
import Transport.ServerRequest
import Transport.ServerResponse
import DataClasses.LoginData
import com.fasterxml.jackson.module.kotlin.*
import io.ktor.application.install
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.sql.SQLException

object Server {
    private val objectMapper = jacksonObjectMapper()
    private fun processRequest(request: ServerRequest): ServerResponse {
        val response = ServerResponse()
        val requestHandler = RequestHandler()
        val withoutToken = arrayOf(TransportType.LOGIN, TransportType.REGISTER)
        if (request.requestType !in withoutToken) {
            if (!TokenHandler().verifyToken(request.jwt)) {
                response.body = "";
                return response
            }
        }
        try {
            when (request.requestType) {
                TransportType.GET_USER -> {
                    val user: User? = requestHandler.getUser(request.id)
                    response.body = objectMapper.writeValueAsString(user)
                }
                TransportType.GET_USER_CHATS -> {
                    val chats: MutableMap<Long, String> = requestHandler.getUserChats(request.id)
                    response.body = objectMapper.writeValueAsString(chats)
                }
                TransportType.GET_CONTACTS -> {
                    val contacts = requestHandler.getUserContacts(request.id)
                    response.body = objectMapper.writeValueAsString(contacts)
                }
                TransportType.REMOVE_USER -> requestHandler.removeUser(request.id, request.body)
                TransportType.EDIT_USER -> requestHandler.editUser(request.id, request.body)
                TransportType.REMOVE_CONTACT -> requestHandler.removeContact(request.id, request.body)
                TransportType.ADD_CONTACT -> requestHandler.addContact(request.id, request.body)

                TransportType.SEND_MESSAGE -> requestHandler.sendMessage(request.body)
                TransportType.EDIT_MESSAGE -> requestHandler.editMessage(request.id, request.body)
                TransportType.DELETE_MESSAGE -> requestHandler.deleteMessage(request.id)
                TransportType.GET_MESSAGE -> {
                    val message: Message? = requestHandler.getMessage(request.id)
                    response.body = objectMapper.writeValueAsString(message)
                }

                TransportType.ADD_CHAT -> requestHandler.addChat(request.body)
                TransportType.GET_CHAT -> {
                    val chat: Chat? = requestHandler.getChat(request.id)
                    response.body = objectMapper.writeValueAsString(chat)
                }
                TransportType.GET_MESSAGES -> {
                    val messages: MutableSet<Message> = requestHandler.getMessages(request.id)
                    response.body = objectMapper.writeValueAsString(messages)
                }
                TransportType.GET_MEMBERS -> {
                    val members: MutableList<Long> = requestHandler.getMembers(request.id)
                    response.body = objectMapper.writeValueAsString(members)
                }
                TransportType.GET_ADMINS -> {
                    val admins: MutableSet<Long> = requestHandler.getAdmins(request.id)
                    response.body = objectMapper.writeValueAsString(admins)
                }
                TransportType.DELETE_CHAT -> requestHandler.deleteChat(request.id)
                TransportType.EDIT_CHAT -> requestHandler.editChat(request.id, request.body)
                TransportType.ADD_MEMBER -> requestHandler.addMember(request.id, request.body)
                TransportType.KICK_MEMBER -> requestHandler.kickMember(request.id, request.body)
                TransportType.ADD_ADMIN -> requestHandler.addAdmin(request.id, request.body)
                TransportType.REMOVE_ADMIN -> requestHandler.removeAdmin(request.id, request.body)

                TransportType.REGISTER -> {
                    val loginData: LoginData = requestHandler.register(request.body)
                    response.body = objectMapper.writeValueAsString(loginData)
                }
                TransportType.LOGIN -> {
                    val loginData: LoginData = requestHandler.login(request.body)
                    response.body = objectMapper.writeValueAsString(loginData)
                }
                TransportType.EDIT_CONTACT -> requestHandler.editContact(request.id, request.body)
                TransportType.BLOCK_USER -> requestHandler.blockUser(request.id, request.body)
                TransportType.UNBLOCK_USER -> requestHandler.unblockUser(request.id, request.body)
                TransportType.GET_BLOCKED_USERS -> {
                    val blockedUsers: MutableSet<Long> = requestHandler.getBlockedUsers(request.id)
                    response.body = Server.objectMapper.writeValueAsString(blockedUsers)
                }
                TransportType.JOIN_CHAT -> TODO()
                TransportType.LEAVE_CHAT -> TODO()
                TransportType.BLOCK_USER_IN_CHAT -> TODO()
                TransportType.UNBLOCK_USER_IN_CHAT -> TODO()
            }
        } catch (se: SQLException) {
                response.status = ResponseStatus.DATABASE_ERROR
                response.body = "Oh no, it's a scary exception!"
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
                            val request: ServerRequest? = objectMapper.readValue(text)
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

