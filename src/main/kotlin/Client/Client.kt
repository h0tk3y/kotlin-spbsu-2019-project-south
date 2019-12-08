package Client

import Chat
import com.fasterxml.jackson.module.kotlin.*
import TransportType.*
import DataClasses.*
import User
import Transport.*
import ServerRequest
import Message
import TransportType
import Transport.ResponseStatus

object Client {

    var webClient = WebClient("127.0.0.1", 9999)

    fun connectTo(host: String = "127.0.0.1", port: Int = 9999) {
        webClient = WebClient(host, port)
    }



    private val objectMapper = jacksonObjectMapper()



    private fun userJSON(user: User): String = objectMapper.writeValueAsString(user)

    private fun messageJSON(message: Message): String = objectMapper.writeValueAsString(message)

    private fun chatJSON(chat: Chat): String = objectMapper.writeValueAsString(chat)



    private var loggedUserId: Long = -1

    fun getLoggedUserId(): Long {
        return loggedUserId
    }



    private fun getResponse(requestType: TransportType, id: Long = -1, body: String = ""): String {
        val response = webClient.makeRequest(ServerRequest(requestType, id, body))
        if (response.status != ResponseStatus.SUCCESSFUL) {
            throw ServerException(response.body)
        }
        return response.body
    }



    class UserDataHandler(private val userId: Long = loggedUserId) {

        private fun getUser(): User = objectMapper.readValue(getResponse(GET_USER, userId))


        fun getName() = getUser().name

        fun getLogin() = getUser().login

        fun getEmail() = getUser().email


        fun getUserChats(): Map<Long, String> = objectMapper.readValue(getResponse(GET_USER_CHATS, userId))

        fun getContacts(): MutableMap<Long, String> = objectMapper.readValue(getResponse(GET_CONTACTS, userId))

        fun getBlockUsers(): MutableMap<Long, String> = objectMapper.readValue(getResponse(GET_BLOCKED_USERS, userId))
        //TODO na servere tolko longi!!!!


        private fun editUser(user: User) = getResponse(
            EDIT_USER,
            userId,
            userJSON(user)
        )


        fun changeName(newName: String) {
            val cur = getUser()
            cur.name = newName
            editUser(cur)
        }

        fun changeEmail(newEmail: String) {
            val cur = getUser()
            cur.email = newEmail
            editUser(cur)
        }


        fun addContact(contactId: Long, name: String = UserDataHandler(contactId).getName()) = getResponse(
            ADD_CONTACT,
            userId,
            userJSON(User(id = contactId, name = name))
        )


        fun editContact(contactId: Long, newName: String) = getResponse(
            EDIT_CONTACT,
            userId,
            userJSON(User(id = contactId, name = newName))
        )


        fun removeContact(contactId: Long) = getResponse(
            REMOVE_CONTACT,
            userId,
            userJSON(User(id = contactId))
        )


        fun blockUser(blockingUserId: Long) = getResponse(
            BLOCK_USER,
            userId,
            userJSON(User(id = blockingUserId))
        )


        fun unblockUser(unblockingUserId: Long) = getResponse(
            UNBLOCK_USER,
            userId,
            userJSON(User(id = unblockingUserId))
        )

    }



    class MessageDataHandler(private val messageId: Long = -1) {

        private fun getMessage(): Message = objectMapper.readValue(getResponse(GET_MESSAGE, messageId))


        fun sendMessage(text: String, chatId: Long, userId: Long) {
            val newMessage = Message(text, -1, chatId, userId)
            getResponse(SEND_MESSAGE, messageId, objectMapper.writeValueAsString(newMessage))
        }


        private fun editMessage(message: Message) {
            message.isEdited = true
            getResponse(EDIT_MESSAGE, messageId, objectMapper.writeValueAsString(message))
        }


        fun getText(): String = getMessage().text

        fun getUserId(): Long = getMessage().userId

        fun getChatId(): Long = getMessage().chatId


        fun editText(newText: String) {
            val cur = getMessage()
            cur.text = newText
            editMessage(cur)
        }


        fun deleteMessage() {
            val cur = getMessage()
            cur.isDeleted = true
            editMessage(cur)
        }

    }



    class ChatDataHandler(private val chatId: Long = -1) {

        private fun getChat(): Chat = objectMapper.readValue(getResponse(GET_CHAT, chatId))


        fun getName() = getChat().name

        fun isSingle() = getChat().isSingle



        fun getMessages() : List<Message>  = objectMapper.readValue(getResponse(GET_MESSAGES, chatId))

        fun getMembers() : List<Long>  = objectMapper.readValue(getResponse(GET_MEMBERS, chatId))

        fun getAdmins() : List<Long>  = objectMapper.readValue(getResponse(GET_ADMINS, chatId))



        private fun editChat(chat: Chat) = getResponse(EDIT_CHAT, chatId, objectMapper.writeValueAsString(chat))


        fun changeChatName(newName: String) {
            val chat = getChat()
            chat.name = newName
            editChat(chat)
        }



        fun createChat(isSingle: Boolean, name: String, members: MutableSet<Long>) {
            val newChat = Chat(-1, isSingle, loggedUserId, name, members)
            getResponse(ADD_CHAT, chatId, objectMapper.writeValueAsString(newChat))
        }



        fun addMember(userId: Long) = getResponse(ADD_MEMBER, chatId, objectMapper.writeValueAsString(userId))



        fun kickMember(userId: Long) = getResponse(KICK_MEMBER, chatId, objectMapper.writeValueAsString(userId))



        fun sendMessage(text: String) = MessageDataHandler().sendMessage(text, chatId, loggedUserId)



        fun addAdmin(userId: Long) = getResponse(ADD_MEMBER, chatId, objectMapper.writeValueAsString(userId))



        fun removeAdmin(userId: Long) = getResponse(REMOVE_ADMIN, chatId, objectMapper.writeValueAsString(userId))

    }

    class LoginDataHandler() {

        fun registerUser(login: String, name: String = login, email: String = "") {
            val newUser = User(-1, login, name)
            newUser.email = email
            val loggedUserData: LoginData = objectMapper.readValue(
                getResponse(REGISTER, -1, objectMapper.writeValueAsString(newUser))
            )
            loggedUserId = loggedUserData.id
            webClient.token = loggedUserData.jwt
        }



        fun loginUser(login: String, password: String) {
            val loggedUserData: LoginData = objectMapper.readValue(
                getResponse(LOGIN, -1, objectMapper.writeValueAsString(LoginData(login, password)))
            )
            loggedUserId = loggedUserData.id
            webClient.token = loggedUserData.jwt
        }
    }
}

