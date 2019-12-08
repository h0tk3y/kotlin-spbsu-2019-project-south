import com.auth0.jwt.exceptions.JWTDecodeException
import transport.ResponseStatus
import transport.ServerResponse
import dataClasses.LoginData
import com.fasterxml.jackson.module.kotlin.*
import consoleUI.getId
import io.ktor.application.install
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import transport.RequestType
import java.lang.Exception
import java.sql.SQLException

object Server {
    private val objectMapper = jacksonObjectMapper()
    private fun processRequest(request: ServerRequest): ServerResponse {
        val response = ServerResponse()
        val requestHandler = RequestHandler()
        val withoutToken = arrayOf(RequestType.LOGIN, RequestType.REGISTER)
        try {
            val senderId = TokenHandler().getId(request.jwt)
            if (request.requestType !in withoutToken) {
                if (!TokenHandler().verifyToken(request.jwt)) {
                    response.body = "Invalid token!"
                    response.status = ResponseStatus.ACCESS_DENIED
                    return response
                }
            }
            try {
                when (request.requestType) {
                    RequestType.GET_USER -> {
                        val user: User? = requestHandler.getUser(request.id)
                        response.body = objectMapper.writeValueAsString(user)
                    }
                    RequestType.GET_USER_CHATS -> {
                        val chats: MutableMap<Long, String> = requestHandler.getUserChats(senderId)
                        response.body = objectMapper.writeValueAsString(chats)
                    }
                    RequestType.GET_CONTACTS -> {
                        val contacts = requestHandler.getUserContacts(senderId)
                        response.body = objectMapper.writeValueAsString(contacts)
                    }
                    RequestType.REMOVE_USER -> requestHandler.removeUser(senderId, request.body)
                    RequestType.EDIT_USER -> requestHandler.editUser(senderId, request.body)
                    RequestType.REMOVE_CONTACT -> requestHandler.removeContact(senderId, request.body)
                    RequestType.ADD_CONTACT -> requestHandler.addContact(senderId, request.body)

                    RequestType.SEND_MESSAGE -> requestHandler.sendMessage(senderId, request.body)
                    RequestType.EDIT_MESSAGE -> requestHandler.editMessage(senderId, request.id, request.body)
                    RequestType.DELETE_MESSAGE -> requestHandler.deleteMessage(senderId, request.id)
                    RequestType.GET_MESSAGE -> {
                        val message: Message? = requestHandler.getMessage(request.id)
                        response.body = objectMapper.writeValueAsString(message)
                    }

                    RequestType.ADD_CHAT -> requestHandler.addChat(request.body)
                    RequestType.GET_CHAT -> {
                        val chat: Chat? = requestHandler.getChat(request.id)
                        response.body = objectMapper.writeValueAsString(chat)
                    }
                    RequestType.GET_MESSAGES -> {
                        val messages: MutableSet<Message> = requestHandler.getMessages(senderId, request.id)
                        response.body = objectMapper.writeValueAsString(messages)
                    }
                    RequestType.GET_MEMBERS -> {
                        val members: MutableList<Long> = requestHandler.getMembers(senderId, request.id)
                        response.body = objectMapper.writeValueAsString(members)
                    }
                    RequestType.GET_ADMINS -> {
                        val admins: MutableSet<Long> = requestHandler.getAdmins(senderId, request.id)
                        response.body = objectMapper.writeValueAsString(admins)
                    }
                    RequestType.DELETE_CHAT -> requestHandler.deleteChat(senderId, request.id)
                    RequestType.EDIT_CHAT -> requestHandler.editChat(senderId, request.id, request.body)
                    RequestType.ADD_MEMBER -> requestHandler.addMember(request.id, request.body)
                    RequestType.KICK_MEMBER -> requestHandler.kickMember(senderId, request.id, request.body)
                    RequestType.ADD_ADMIN -> requestHandler.addAdmin(senderId, request.id, request.body)
                    RequestType.REMOVE_ADMIN -> requestHandler.removeAdmin(senderId, request.id, request.body)

                    RequestType.REGISTER -> {
                        val loginData: LoginData = requestHandler.register(request.body)
                        response.body = objectMapper.writeValueAsString(loginData)
                    }
                    RequestType.LOGIN -> {
                        val loginData: LoginData = requestHandler.login(request.body)
                        response.body = objectMapper.writeValueAsString(loginData)
                    }
                    RequestType.EDIT_CONTACT -> requestHandler.editContact(request.id, request.body)
                    RequestType.BLOCK_USER -> requestHandler.blockUser(request.id, request.body)
                    RequestType.UNBLOCK_USER -> requestHandler.unblockUser(request.id, request.body)
                    RequestType.GET_BLOCKED_USERS -> {
                        val blockedUsers: MutableSet<Long> = requestHandler.getBlockedUsers(request.id)
                        response.body = objectMapper.writeValueAsString(blockedUsers)
                    }
                    RequestType.LEAVE_CHAT -> requestHandler.leaveChat(senderId, request.id, request.body)
                }
            } catch (se: Exception) {
                when (se) {
                    is SQLException -> {
                        response.status = ResponseStatus.DATABASE_ERROR
                        response.body = "Oh no, it's a scary database exception!"
                    }
                    is ServerException -> {
                        response.status = ResponseStatus.ACCESS_DENIED
                        response.body = se.message!!
                    }
                }

            }
            return response
        } catch (e: JWTDecodeException) {
            response.body = "Invalid token!"
            response.status = ResponseStatus.ACCESS_DENIED
            return response
        }
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

