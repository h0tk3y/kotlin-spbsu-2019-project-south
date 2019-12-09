import dataBases.DataBaseHandler
import dataClasses.LoginData
import dataBases.PasswordBase
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.sql.SQLException

class RequestHandler() {
    private val objectMapper = jacksonObjectMapper()
    private val handler = DataBaseHandler()
    private var userBase: UserBase
    private var chatBase: ChatBase
    private var messageBase: MessageBase
    private var passwordBase : PasswordBase //TODO

    init {
        handler.initAll()
        this.userBase = UserBase(this.handler.connection!!)
        this.chatBase = ChatBase(this.handler.connection!!)
        this.messageBase = MessageBase(this.handler.connection!!)
        this.passwordBase = PasswordBase(this.handler.connection!!) //TODO
    }




    fun getUser(userId: Long): User? {
        try {
            return userBase.get(userId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getUserChats(userId: Long): MutableMap<Long, String> {

        try {
            return userBase.getChats(userId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getUserContacts(userId: Long): MutableMap<Long, String> {
        try {
            return userBase.getContacts(userId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun removeUser(chatId: Long, userIdString: String) {
        val userId: Long = objectMapper.readValue(userIdString)
        try {
            chatBase.removeMember(chatId, userId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editUser(userId: Long, userString: String) {
        val user: User = objectMapper.readValue(userString)
        try {
            userBase.edit(userId, user)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun removeContact(userId: Long, contactString: String) {
        val contact: User = objectMapper.readValue(contactString)
        try {
            userBase.removeContact(userId, contact.id, contact.name)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addContact(userId: Long, contactString: String) {
        val contact: User = objectMapper.readValue(contactString)
        if (!userBase.isBlocked(contact.id, userId)) {
            try {
                userBase.addContact(userId, contact.id, contact.name)
            } catch (se: SQLException) {
                throw se
            }
        } else {
            throw ServerException("User is blocked")
        }
    }




    fun sendMessage(senderId: Long, messageString : String) {
        val message : Message = objectMapper.readValue(messageString)
        if (senderId !in chatBase.getMembers(message.chatId)) {
            throw ServerException("Not a chat member")
        }
        try {
            messageBase.add(message)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editMessage(senderId: Long, messageId: Long, messageString: String) {
        val message: Message = objectMapper.readValue(messageString)
        if (message.userId != senderId) {
            throw ServerException("Invalid editor")
        }
        try {
            messageBase.edit(messageId, message)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun deleteMessage(senderId: Long, messageId: Long) {
        val message: Message? = getMessage(messageId)
        try {
            if (message != null) {
                if (message.userId != senderId && senderId !in getAdmins(senderId, message.chatId)) {
                    throw ServerException("Invalid editor")
                }
            }
            messageBase.remove(messageId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getMessage(messageId: Long): Message? {
        try {
            return messageBase.get(messageId)
        } catch (se: SQLException) {
            throw se
        }
    }




    fun addChat(chatString: String) : Long {
        val chat: Chat = objectMapper.readValue(chatString)
        try {
            val id = chatBase.add(chat)
            return id
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getChat(chatId: Long): Chat? {
        try {
            return chatBase.get(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getMessages(senderId: Long, chatId: Long): MutableSet<Message> {
        try {
            if (senderId !in getMembers(senderId, chatId)) {
                throw ServerException("Not a chat member")
            }
            return chatBase.getMessages(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getMembers(senderId: Long, chatId: Long): MutableList<Long> {
        try {
            return chatBase.getMembers(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getAdmins(senderId: Long, chatId: Long): MutableSet<Long> {
        try {
            return chatBase.getAdmins(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun deleteChat(senderId: Long, chatId: Long) {
        try {
            if (chatBase.get(chatId)!!.owner_id != senderId) {
                throw  ServerException("Not an owner")
            }
            chatBase.remove(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editChat(senderId: Long, chatId: Long, chatString: String) {
        val chat: Chat = objectMapper.readValue(chatString)
        try {
            if (senderId !in getAdmins(senderId, chatId)) {
                throw ServerException("Not an admin")
            }
            chatBase.edit(chatId, chat)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addMember(chatId: Long, memberIdString: String) {
        val memberId: Long = objectMapper.readValue(memberIdString)
        try {
            chatBase.addMember(chatId, memberId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun kickMember(senderId: Long, chatId: Long, memberIdString: String) {
        val memberId: Long = objectMapper.readValue(memberIdString)
        try {
            if (senderId !in getAdmins(senderId, chatId) && senderId != chatBase.get(chatId)!!.owner_id) {
                throw ServerException("Not an admin")
            }
            chatBase.removeMember(chatId, memberId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addAdmin(senderId: Long, chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        try {
            if (chatBase.get(chatId)!!.owner_id != senderId) {
                throw  ServerException("Not an owner")
            }
            chatBase.addAdmin(chatId, adminId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun removeAdmin(senderId: Long, chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        try {
            if (chatBase.get(chatId)!!.owner_id != senderId) {
                throw  ServerException("Not an owner")
            }
            chatBase.removeAdmin(chatId, adminId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun register(loginDataString: String) : LoginData {
        val userLoginData: LoginData = objectMapper.readValue(loginDataString)
        try {
            passwordBase.add(userLoginData.login, userLoginData.password)
            val userId = userBase.add(userLoginData.user)
            userLoginData.user.id = userId
            userLoginData.id = userId
            userLoginData.jwt = TokenHandler().makeToken(userLoginData.user)
        }
        catch (se: SQLException) {
            throw ServerException("Login already exists")
        }
        return userLoginData
    }

    fun login(loginDataString : String) : LoginData {
        val userLoginData : LoginData = objectMapper.readValue(loginDataString)
        try {
            if (passwordBase.checkPassword(userLoginData.login, userLoginData.password)) {
                val user: User = userBase.findByLogin(userLoginData.login) ?: throw ServerException("User not found")
                userLoginData.jwt = TokenHandler().makeToken(userBase.get(user.id) ?: throw ServerException("User not found"))

                userLoginData.id = user.id
            }
            else {
                throw ServerException("Wrong login or password")
            }
        }
        catch (se: SQLException) {
            throw se
        }
        return userLoginData
    }

    fun editContact(userId: Long, contactString: String) {
        val newContact : User = objectMapper.readValue(contactString)
        try {
            userBase.editContact(userId, newContact.id, newContact.name)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun blockUser(userId: Long, blockedUserIdString: String) {
        val blockedUserId: Long = objectMapper.readValue<User>(blockedUserIdString).id
        try {
            userBase.blockUser(userId, blockedUserId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun unblockUser(userId: Long, userToUnlockIdString: String) {
        val userToUnlockId: Long = objectMapper.readValue<User>(userToUnlockIdString).id
        try {
            userBase.unblockUser(userId, userToUnlockId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getBlockedUsers(userId: Long): MutableSet<Long> {
        try {
            return userBase.getBlocked(userId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun leaveChat(senderId: Long, chatId: Long, userString: String) {
        val memberId: Long = objectMapper.readValue<User>(userString).id
        try {
            if (senderId !in getMembers(senderId, chatId)) {
                throw ServerException("Not an admin")
            }
            chatBase.removeMember(chatId, memberId)
        } catch (se: SQLException) {
            throw se
        }
    }



    fun isAdmin(senderId: Long, chatId: Long) : Boolean {
        try {
            return senderId in getAdmins(senderId, chatId) || isOwner(senderId, chatId)
        } catch (e: SQLException) {
            throw e
        }
    }

    fun isMember(senderId: Long, chatId: Long) : Boolean {
        try {
            return senderId in getMembers(senderId, chatId)
        } catch (e: SQLException) {
            throw e
        }
    }

    fun isOwner(senderId: Long, chatId: Long) : Boolean {
        try {
            return chatBase.get(chatId)!!.owner_id == senderId
        } catch (e: SQLException) {
            throw e
        }
    }
}