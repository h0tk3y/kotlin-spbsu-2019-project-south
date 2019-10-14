import com.fasterxml.jackson.module.kotlin.*
import TransportType.*
import FieldType.*

object Client {
    private var loggedUserId: Long = -1

    private val webClient = WebClient("lol", 10)

    fun getLoggedUserId(): Long {
        return loggedUserId
    }

    fun registerUser(login: String, name: String = login, email: String = "") {
        loggedUserId = UserData().addUser(login, name, email)
    }

    class UserData(var userId: Long = loggedUserId) {
        private val objectMapper = jacksonObjectMapper()

        private fun getUser(id: Long): User {
            return objectMapper.readValue<User>(webClient.makeRequest(ServerRequest(GET, USER, id)).body)
        }

        private fun editUser(user: User) {
            ServerRequest(EDIT, USER, userId, objectMapper.writeValueAsString(user)).makeRequest()
        }

        fun addUser(login: String, name: String, email: String): Long {
            val newUser = User(-1, login)
            newUser.name = name
            newUser.email = email
            return objectMapper.readValue<Long>(
                ServerRequest(ADD, USER, userId, objectMapper.writeValueAsString(newUser)).makeRequest()
            )
        }

        fun addContact(contactId: Long, name: String = "") {
            val cur = getUser(userId)
            if (name == "") {
                val newUser = getUser(contactId)
                cur.contacts[contactId] = newUser.name
            } else {
                cur.contacts[contactId] = name
            }
            editUser(cur)
        }

        fun changeContact(contactId: Long, name: String) {
            val cur = getUser(userId)
            cur.contacts[contactId] = name
            editUser(cur)
        }

        fun deleteContact(contactId: Long) {
            val cur = getUser(userId)
            cur.contacts.remove(contactId)
            editUser(cur)
        }

        fun addBlockedUser(blockedUserId: Long) {
            val cur = getUser(userId)
            cur.blockedUsers.add(blockedUserId)
            editUser(cur)
        }

        fun deleteBlockedUser(blockedUserId: Long) {
            val cur = getUser(userId)
            cur.blockedUsers.remove(blockedUserId)
            editUser(cur)
        }

        fun addChat(chatId: Long, chatName: String = chatId.toString()) {
            val cur = getUser(userId)
            cur.chatsId[chatId] = chatName
            editUser(cur)
        }

        fun deleteChat(chatId: Long) {
            val cur = getUser(userId)
            cur.chatsId.remove(chatId)
            editUser(cur)
        }

        fun changeName(newName: String) {
            val cur = getUser(userId)
            cur.name = newName
            editUser(cur)
        }

        fun changeEmail(newEmail: String) {
            val cur = getUser(userId)
            cur.email = newEmail
            editUser(cur)
        }

        fun getName() = getUser(userId).name

        fun getLogin() = getUser(userId).login

        fun getEmail() = getUser(userId).email

        fun getChats() = getUser(userId).chatsId

        fun getBlockedUsers() = getUser(userId).blockedUsers

        fun getContacts() = getUser(userId).contacts
    }

    class MessageData(private val messageId: Long = -1) {
        private val objectMapper = jacksonObjectMapper()

        private fun getMessage(): Message =
            objectMapper.readValue<Message>(ServerRequest(GET, MESSAGE, messageId).makeRequest())

        private fun editMessage(message: Message) =
            ServerRequest(EDIT, MESSAGE, messageId, objectMapper.writeValueAsString(message)).makeRequest()

        private fun addMessage(message: Message) =
            ServerRequest(ADD, MESSAGE, messageId, objectMapper.writeValueAsString(message)).makeRequest()

        fun createMessage(text: String, chatId: Long, userId: Long): Long {
            val newMessage = Message(text, -1, chatId, userId)
            return objectMapper.readValue<Long>(addMessage(newMessage))
        }

        fun getText(): String = getMessage().text
        fun getUserId(): Long = getMessage().userId
        fun getChatId(): Long = getMessage().chatId

        fun editText(newText: String) {
            val cur = getMessage()
            cur.text = newText
            cur.isEdited = true
            editMessage(cur)
        }

        fun deleteMessage() {
            val cur = getMessage()
            cur.isDeleted = true
            editMessage(cur)
        }

        fun deleteMessageForever() = ServerRequest(
            REMOVE,
            MESSAGE,
            messageId,
            objectMapper.writeValueAsString(getMessage())
        ).makeRequest()

        fun readMessage() {
            val cur = getMessage()
            cur.isRead = true
            editMessage(cur)
        }
    }

    class ChatData(private val chatId: Long = -1) {
        private val objectMapper = jacksonObjectMapper()

        private fun getChat(): Chat {
            return objectMapper.readValue<Chat>(ServerRequest(GET, CHAT, chatId).makeRequest())
        }

        private fun editChat(chat: Chat) {
            ServerRequest(EDIT, CHAT, chatId, objectMapper.writeValueAsString(chat)).makeRequest()
        }

        fun createChat(isSingle: Boolean, name: String, owners: MutableSet<Long>): Long {
            val newChat = Chat(-1, isSingle, name, owners)
            if (isSingle) {
                val userId1 = owners.first()
                val userId2 = owners.last()
                UserData(userId1).addChat(chatId, UserData(userId2).getName())
                UserData(userId2).addChat(chatId, UserData(userId1).getName())
            } else {
                UserData(owners.first()).addChat(chatId, name)
            }
            return objectMapper.readValue<Long>(
                ServerRequest(ADD, CHAT, chatId, objectMapper.writeValueAsString(newChat)).makeRequest()
            )
        }

        fun addUser(userId: Long) {
            val chat = getChat()
            if (!chat.isSingle) {
                chat.members.add(userId)
                UserData(userId).addChat(chatId, chat.name)
                editChat(chat)
            }
        }

        fun deleteUser(userId: Long) {
            val chat = getChat()
            if (!chat.isSingle) {
                if (userId !in chat.owners) {
                    chat.members.remove(userId)
                    UserData(userId).deleteChat(chatId)
                    editChat(chat)
                }
            }
        }

        fun sendMessage(messageId: Long) {
            val chat = getChat()
            chat.messages.add(messageId)
            editChat(chat)
        }

        fun editMessage(messageId: Long, text: String) = MessageData(messageId).editText(text)

        fun deleteMessage(messageId: Long) = MessageData(messageId).deleteMessage()

        fun getMembers() = getChat().members

        fun getMessages() = getChat().messages
    }
}
