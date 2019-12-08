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
        try {
            userBase.addContact(userId, contact.id, contact.name)
        } catch (se: SQLException) {
            throw se
        }
    }




    fun sendMessage(messageString : String) {
        val message : Message = objectMapper.readValue(messageString)
        try {
            messageBase.add(message)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editMessage(messageId: Long, messageString: String) {
        val message: Message = objectMapper.readValue(messageString)
        try {
            messageBase.edit(messageId, message)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun deleteMessage(messageId: Long) {
        try {
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




    fun addChat(chatString: String) {
        val chat: Chat = objectMapper.readValue(chatString)
        try {
            chatBase.add(chat)
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

    fun getMessages(chatId: Long): MutableSet<Message>{
        try {
            return chatBase.getMessages(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getMembers(chatId: Long): MutableList<Long> {
        try {
            return chatBase.getMembers(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun getAdmins(chatId: Long): MutableSet<Long> {
        try {
            return chatBase.getAdmins(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun deleteChat(chatId: Long) {
        try {
            chatBase.remove(chatId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun editChat(chatId: Long, chatString: String) {
        val chat: Chat = objectMapper.readValue(chatString)
        try {
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

    fun kickMember(chatId: Long, memberIdString: String) {
        val memberId: Long = objectMapper.readValue(memberIdString)
        try {
            chatBase.removeMember(chatId, memberId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun addAdmin(chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        try {
            chatBase.addAdmin(chatId, adminId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun removeAdmin(chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        try {
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
            throw se
        }
        return userLoginData
    }

    fun login(loginDataString : String) : LoginData {
        val userLoginData : LoginData = objectMapper.readValue(loginDataString)
        try {
            if (passwordBase.checkPassword(userLoginData.login, userLoginData.password)) {
                val user: User = userBase.findByLogin(userLoginData.login);
                userLoginData.jwt = TokenHandler().makeToken(userBase.get(user.id) ?: throw TODO())
                userLoginData.id = user.id
            }
        }
        catch (se: SQLException) {
            throw se
        }
        return userLoginData
    }

    fun editContact(userId: Long, contactString: String) {
        val newContact: Pair<Long, String> = objectMapper.readValue(contactString)
        try {
            userBase.editContact(userId, newContact.first, newContact.second)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun blockUser(userId: Long, blockedUserIdString: String) {
        val blockedUserId: Long = objectMapper.readValue(blockedUserIdString)
        try {
            userBase.blockUser(userId, blockedUserId)
        } catch (se: SQLException) {
            throw se
        }
    }

    fun unblockUser(userId: Long, userToUnlockIdString: String) {
        val userToUnlockId: Long = objectMapper.readValue(userToUnlockIdString)
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

    fun joinChat(chatId: Long, userString: String) {
        TODO()
    }

    fun leaveChat(chatId: Long, userString: String) {
        TODO()
    }

    fun blockUserInChat(chatId: Long, userString: String) {
        TODO()
    }

    fun unblockUserInChat() {
        TODO()
    }
}