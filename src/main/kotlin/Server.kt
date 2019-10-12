import com.fasterxml.jackson.module.kotlin.*
class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")

    fun answerRequest(requestString: String) : String {
        val request: ServerRequest = ServerRequest(requestString)

        when (request.fieldType) {
            FieldType.USER -> {
                val user: User = objectMapper.readValue<User>(request.body)
                when (request.requestType) {
                    RequestType.GET -> return objectMapper.writeValueAsString(userBase.get(user.id))
                    RequestType.ADD -> userBase.add(user)
                    RequestType.REMOVE -> userBase.remove(user.id)
                    RequestType.EDIT -> userBase.edit(user.id, user)
                }
            }
            FieldType.CHAT -> {
                val chat: Chat = objectMapper.readValue<Chat>(request.body)
                when (request.requestType) {
                    RequestType.GET -> return objectMapper.writeValueAsString(chatBase.get(chat.id))
                    RequestType.ADD -> chatBase.add(chat)
                    RequestType.REMOVE -> chatBase.remove(chat.id)
                    RequestType.EDIT -> chatBase.edit(chat.id, chat)
                }
            }
            FieldType.MESSAGE -> {
                val message: Message = objectMapper.readValue<Message>(request.body)
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
}

