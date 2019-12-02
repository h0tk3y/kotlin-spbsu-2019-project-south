import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class RequestHandler {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")


    fun getUser(userId : Long) : User? {
        return userBase.get(userId)
    }

    fun sendMessage(messageString : String) {
        val message : Message = objectMapper.readValue(messageString)
        messageBase.add(message)
    }

    fun getChat(chatId : Long) : Chat? {
        return chatBase.get(chatId)
    }

    fun getMessages(chatId : Long, countString : String) : List<Message>{
        val count : Long = objectMapper.readValue(countString)
        return chatBase.getMessages(chatId, count)
    }

    fun deleteMessage(MessageId : Long) {
        messageBase.remove(MessageId)
    }

    fun deleteChat(chatId : Long) {
        chatBase.remove(chatId)
    }

    fun editMessage(messageId : Long, messageString : String) {
        val message : Message = objectMapper.readValue(messageString)
        messageBase.edit(messageId, message)
    }

    fun editUser(userId : Long, userString : String) {
        val user : User = objectMapper.readValue(userString)
        userBase.edit(userId, user)
    }

    fun getUserChats(userId : Long) : List<Chat> {
        return chatBase.getChats(userId)
    }

    fun addChat(chatString : String) {
        val chat : Chat = objectMapper.readValue(chatString)
        chatBase.add(chat)
    }

    fun removeUser(chatId : Long, userIdString : String) {
        val userId : Long = objectMapper.readValue(userIdString)
        var chat = chatBase.get(chatId)
        chat?.members?.remove(userId)
        if (chat != null) {
            chatBase.edit(chatId, chat)
        }
    }

    fun editChat(chatId : Long, chatString : String) {
        val chat : Chat = objectMapper.readValue(chatString)
        chatBase.edit(chatId, chat)
    }

    fun addMember(chatId : Long, memberString : String) {
        val member : User = objectMapper.readValue(memberString)
        val chat = chatBase.get(chatId)
        chat?.members?.add(member.id)
        if (chat != null) {
            chatBase.edit(chatId, chat)
        }
    }

    fun addAdmin(chatId : Long, memberString : String) {
        val member : User = objectMapper.readValue(memberString)
        val chat = chatBase.get(chatId)
        chat?.owners?.add(member.id)
        if (chat != null) {
            chatBase.edit(chatId, chat)
        }
    }
}