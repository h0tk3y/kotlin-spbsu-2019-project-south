import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.`KotlinPackage-Extensions-6da2b0d6`.jacksonObjectMapper

class Server {
    private val objectMapper = jacksonObjectMapper()

    private var userBase : UserBase = UserBase("users.db")
    private var chatBase : ChatBase = ChatBase("chats.db")
    private var messageBase : MessageBase = MessageBase("messages.db")

    fun answerRequest(requestString: String) : String {
        val request : ServerRequest = ServerRequest(requestString)

        when (request.fieldType) {
            FieldType.USER -> {
                val user : User = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(userBase.get(user.id))
                    ReqType.ADD -> userBase.add(user)
                    ReqType.REMOVE -> userBase.remove(user.id)
                    ReqType.EDIT -> userBase.edit(user)
                }
            }
            FieldType.CHAT -> {
                val chat : Chat = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(chatBase.get(chat.id))
                    ReqType.ADD -> userBase.add(chat)
                    ReqType.REMOVE -> userBase.remove(chat.id)
                    ReqType.EDIT -> userBase.edit(chat)
                }
            }
            FieldType.MESSAGE -> {
                val message : Message = objectMapper.readValue(request.body)
                when (request.reqType) {
                    ReqType.GET -> return objectMapper.writeValueAsString(messageBase.get(message.id))
                    ReqType.ADD -> messageBase.add(message)
                    ReqType.REMOVE -> messageBase.remove(message.id)
                    ReqType.EDIT -> userBase.edit(message)
                }
            }
        }
    }
}