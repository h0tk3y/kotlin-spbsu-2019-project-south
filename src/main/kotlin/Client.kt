import com.fasterxml.jackson.module.kotlin.*
import RequestType.*
import FieldType.*

class Client() {
    var loggedUserId: Long = -1

    fun registerUser(login : String, name : String = login, email : String = "") {
        loggedUserId = UserData().addUser(login, name, email)
    }

    class UserData(var userId: Long = -1) { //TODO: getSmth()

        private val objectMapper = jacksonObjectMapper()

        private fun getUser(id : Long) : User {
            return objectMapper.readValue<User>(ServerRequest(GET, USER, id).makeRequest())
        }

        private fun editUser(user : User) {
            ServerRequest(EDIT, USER, userId, objectMapper.writeValueAsString(user)).makeRequest()
        }

        fun addUser(login : String, name : String, email : String) : Long {
            val newUser = User(-1, login)
            newUser.name = name
            newUser.email = email
            return objectMapper.readValue<Long>(
                ServerRequest(ADD, USER, userId, objectMapper.writeValueAsString(newUser)).makeRequest())
        }

        fun addContact(contactId: Long, name : String = ""){
            val cur = getUser(userId)
            if (name == "") {
                val newUser = getUser(contactId)
                cur.contacts[contactId] = newUser.name
            }
            else {
                cur.contacts[contactId] = name
            }
            editUser(cur)
        }

        fun changeContact(contactId : Long, name : String) {
            val cur = getUser(userId)
            cur.contacts[contactId] = name
            editUser(cur)
        }

        fun deleteContact(contactId: Long) {
            val cur = getUser(userId)
            cur.contacts.remove(contactId)
            editUser(cur)
        }

        fun addBlockedUser(blockedUserId: Long){
            val cur = getUser(userId)
            cur.blockedUsers.add(blockedUserId)
            editUser(cur)
        }

        fun deleteBlockedUser(blockedUserId: Long) {
            val cur = getUser(userId)
            cur.blockedUsers.remove(blockedUserId)
            editUser(cur)
        }

        fun addChat(chatId : Long){
            val cur = getUser(userId)
            cur.chatsId.add(chatId)
            editUser(cur)
        }

        fun deleteChat(chatId : Long) {
            val cur = getUser(userId)
            cur.chatsId.remove(chatId)
            editUser(cur)
        }

        fun changeName(newName : String) {
            val cur = getUser(userId)
            cur.name = newName
            editUser(cur)
        }

        fun changeEmail(newEmail : String) {
            val cur = getUser(userId)
            cur.email = newEmail
            editUser(cur)
        }

        fun getName() : String{
            TODO()
        }

    }

    class MessageData(private val messageId : Long = -1) {
        private val objectMapper = jacksonObjectMapper()

        private fun getMessage(id: Long): Message {
            return objectMapper.readValue<User>(ServerRequest(GET, MESSAGE, id).makeRequest())
        }

        private fun editMessage(message: Message) {
            ServerRequest(EDIT, MESSAGE, message.id, objectMapper.writeValueAsString<Message>(message)).makeRequest()
        }

        private fun addMessage(message: Message) =
            ServerRequest(ADD, MESSAGE, messageId, objectMapper.writeValueAsString<Message>(message)).makeRequest()

        fun createMessage(text : String, chatId: Long, userId: Long) : Long {
            val newMessage = Message(text, -1, chatId, userId)
            return objectMapper.readValue<Long>(addMessage(newMessage))
        }

        fun getText() : String = getMessage(messageId).text
        fun getUserId() : Long = getMessage(messageId).userId
        fun getChatId() : Long = getMessage(messageId).chatId

        fun editText(newText: String) {
            val cur = getMessage(messageId)
            cur.text = newText
            cur.isEdited = true
            editMessage(cur)
        }

        fun deleteMessage() {
            val cur = getMessage(messageId)
            cur.isDeleted = true
            editMessage(cur)
        }

        fun deleteMessageForever() = ServerRequest(
            REMOVE,
            MESSAGE,
            messageId,
            objectMapper.writeValueAsString<Message>(getMessage(messageId))
        ).makeRequest()

        fun readMessage() {
            val cur = getMessage(messageId)
            cur.isRead = true
            editMessage(cur)
        }
    }

    class ChatData(val chatId: Long = -1) { //TODO: getSmth()
        private val objectMapper = jacksonObjectMapper()

        private fun getChat(id : Long) : Chat {
            return objectMapper.readValue<Chat>(ServerRequest(GET, CHAT, id).makeRequest())
        }

        private fun editChat(chat : Chat) {
            ServerRequest(EDIT, CHAT, chatId, objectMapper.writeValueAsString(chat)).makeRequest()
        }

        fun createChat(isSingle: Boolean, name: String, owners: MutableSet<Long>) : Long {
            val newChat = Chat(-1, isSingle, name, owners)
            if (isSingle) {
                val userId1 = owners.first()
                val userId2 = owners.last()
                //UserData(userId1).addChat(chatId, UserData(userId2).getName())
                //UserData(userId2).addChat(chatId, UserData(userId1).getName())
                UserData(userId1).addChat(chatId)
                UserData(userId2).addChat(chatId)
            } else {
                //UserData(owners.first()).addChat(chatId, name)
                UserData(owners.first()).addChat(chatId)
            }
            return objectMapper.readValue<Long>(
                ServerRequest(ADD, CHAT, chatId, objectMapper.writeValueAsString(newChat)).makeRequest()
            )
        }

        fun addUser(userId: Long) {
            val chat = getChat(chatId)
            chat.members.add(userId)
            //UserData(userId).addChat(chatId, chat.name)
            UserData(userId).addChat(chatId)
            editChat(chat)
        }

        fun deleteUser(userId: Long) {
            val chat = getChat(chatId)
            if (userId !in chat.owners) {
                chat.members.remove(userId)
                UserData(userId).deleteChat(chatId)
                editChat(chat)
            }
        }

/*        fun sendNotifications(chat: Chat, senderId : Long) {
            for (receiver in chat.members - mutableSetOf(senderId))
                UserData(receiver).getNotification(chatId)
        }   */

        fun sendMessage(text: String, userId: Long) {
            val chat = getChat(chatId)
            val messageId = MessageData().createMessage(text, chatId, userId)
            chat.messages.add(messageId)
            editChat(chat)
            //sendNotifications(chat, userId)
        }

        fun editMessage(messageId: Long, text: String) {
            val chat = getChat(chatId)
            MessageData(messageId).editText(text)
            //sendNotifications(chat, MessageData(messageId).getUserId())
        }

        fun deleteMessage(messageId: Long) {
            val chat = getChat(chatId)
            MessageData(messageId).deleteMessage()
            //sendNotifications(chat, MessageData(messageId).getUserId())
        }
    }
}
