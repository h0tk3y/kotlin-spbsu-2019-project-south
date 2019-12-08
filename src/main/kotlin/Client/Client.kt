package Client

import Chat
import com.fasterxml.jackson.module.kotlin.*
import TransportType.*
import DataClasses.*

object Client {
    private var webClient = WebClient("127.0.0.1", 9999)

    fun connectTo(host: String = "127.0.0.1", port: Int = 9999) {
        webClient = WebClient(host, port)
    }

    private val objectMapper = jacksonObjectMapper()

    private var loggedUserId: Long = -1

    fun getLoggedUserId(): Long {
        return loggedUserId
    }

    class UserDataHandler(private val userId: Long = loggedUserId) {

        private fun getUser(id: Long): User {
            return objectMapper.readValue(
                webClient.makeRequest(
                    ServerRequest(
                        GET_USER, userId, objectMapper.writeValueAsString(User(id))
                    )
                ).body
            )
        }

        private fun editUser(user: User) {
            webClient.makeRequest(
                ServerRequest(
                    EDIT_USER,
                    userId,
                    objectMapper.writeValueAsString(user)
                )
            )
        }

        fun addContact(contactId: Long, name: String = UserDataHandler(contactId).getName()) {
            val newContact = UserContact(userId, contactId)
            newContact.name = name
            webClient.makeRequest(
                ServerRequest(
                    ADD_CONTACT,
                    userId,
                    objectMapper.writeValueAsString(newContact)
                )
            )
        }

        fun changeContactName(contactId: Long, newName: String) {
            val editedContact = UserContact(userId, contactId)
            editedContact.name = newName
            webClient.makeRequest(
                ServerRequest(
                    EDIT_CONTACT,
                    userId,
                    objectMapper.writeValueAsString(editedContact)
                )
            )
        }

        fun removeContact(contactId: Long) {
            val removingContact = UserContact(userId, contactId)
            webClient.makeRequest(
                ServerRequest(
                    REMOVE_CONTACT,
                    userId,
                    objectMapper.writeValueAsString(removingContact)
                )
            )
        }

        fun blockUser(blockedUserId: Long) {
            val newBlockedUser = UserContact(userId, blockedUserId)
            webClient.makeRequest(
                ServerRequest(
                    BLOCK_USER,
                    userId,
                    objectMapper.writeValueAsString(newBlockedUser)
                )
            )
        }

        fun unblockUser(blockedUserId: Long) {
            val unblockingUser = UserContact(userId, blockedUserId)
            webClient.makeRequest(
                ServerRequest(
                    UNBLOCK_USER,
                    userId,
                    objectMapper.writeValueAsString(unblockingUser)
                )
            )
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

        fun getChats(): List<Chat> = objectMapper.readValue(
            webClient.makeRequest(ServerRequest(GET_USER_CHATS, userId)).body
        )

        fun getContacts() : MutableMap<Long, String> = objectMapper.readValue(
            webClient.makeRequest(ServerRequest(GET_CONTACTS, userId)).body
        )

        fun getBlockedUsers() : MutableSet<Long> = objectMapper.readValue(
            webClient.makeRequest(ServerRequest(GET_BLOCKED_USERS, userId)).body
        )
    }

    class MessageDataHandler(private val messageId: Long = -1) {

        private fun getMessage(): Message =
            objectMapper.readValue(
                webClient.makeRequest(
                    ServerRequest(
                        GET_MESSAGE,
                        messageId,
                        objectMapper.writeValueAsString(Message(id = messageId))
                    )
                ).body
            )

        private fun editMessage(message: Message) =
            webClient.makeRequest(
                ServerRequest(
                    EDIT_MESSAGE,
                    messageId,
                    objectMapper.writeValueAsString(message)
                )
            )

        private fun sendMessage(message: Message) =
            webClient.makeRequest(
                ServerRequest(
                    SEND_MESSAGE,
                    body = objectMapper.writeValueAsString(message)
                )
            ).body

        fun createMessage(text: String, chatId: Long, userId: Long): Long {
            val newMessage = Message(text, -1, chatId, userId)
            return objectMapper.readValue(sendMessage(newMessage))
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

      /*  fun readMessage() {
            val cur = getMessage()
            cur.isRead = true
            editMessage(cur)
        } */
    }

    // TODO: REFACTOR CHAT HANDLER!
  /*  class ChatDataHandler(private val chatId: Long = -1) {

        private fun getChat(): Chat {
            return objectMapper.readValue(webClient.makeRequest(ServerRequest(GET_CHAT, chatId)).body)
        }

        private fun editChat(chat: Chat) {
            webClient.makeRequest(ServerRequest(EDIT_CHAT, chatId, objectMapper.writeValueAsString(chat)))
        }

        fun createChat(isSingle: Boolean, name: String, members: MutableSet<Long>): Long {
            val newChat = Chat(-1, isSingle, name, members)
            return objectMapper.readValue(
                webClient.makeRequest(ServerRequest(ADD_CHAT, chatId, objectMapper.writeValueAsString(newChat))).body
            )
        }

        fun changeChatName(newName: String) {
            val chat = getChat()
            if (!chat.isSingle) {
                chat.name = newName
                editChat(chat)
            }
        }

        fun addUser(userId: Long) {
            val chat = getChat()
            if (!chat.isSingle) {
                chat.members.add(userId)
                UserDataHandler(userId).addChat(chatId, chat.name)
                editChat(chat)
            }
        }

        fun deleteUser(userId: Long) {
            val chat = getChat()
            if (!chat.isSingle) {
                if (userId !in chat.owners) {
                    chat.members.remove(userId)
                    UserDataHandler(userId).deleteChat(chatId)
                    editChat(chat)
                }
            }
        }

        fun sendMessage(messageId: Long) {
            val chat = getChat()
            chat.messages.add(messageId)
            editChat(chat)
        }

        fun editMessage(messageId: Long, text: String) = MessageDataHandler(messageId).editText(text)

        fun deleteMessage(messageId: Long) = MessageDataHandler(messageId).deleteMessage()

        fun getMembers() = getChat().members

        fun getName() = getChat().name

        fun getMessages() = getChat().messages

        fun isSingle() = getChat().isSingle
    } */

    class LoginDataHandler() {

        fun registerUser(login: String, name: String = login, email: String = "") {
            val newUser = User(-1, login, name)
            newUser.email = email
            loggedUserId = objectMapper.readValue(
                webClient.makeRequest(
                    ServerRequest(
                        REGISTER,
                        body = objectMapper.writeValueAsString(newUser)
                    )
                ).body
            )
        }

        fun loginUser(login: String, password: String) {
            loggedUserId = objectMapper.readValue(
                webClient.makeRequest(
                    ServerRequest(
                        LOGIN, body = objectMapper.writeValueAsString(
                            LoginData(login, password)
                        )
                    )
                ).body
            )
        }
    }
}
