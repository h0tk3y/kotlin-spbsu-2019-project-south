import DataBases.DataBaseHandler
import DataClasses.LoginData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.lang.reflect.Member

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
        return userBase.get(userId)
    }

    fun getUserChats(userId: Long): MutableMap<Long, String> {
        return userBase.getChats(userId)
    }

    fun getUserContacts(userId: Long): MutableMap<Long, String> {
        return userBase.getContacts(userId)
    }

    fun removeUser(chatId: Long, userIdString: String) {
        val userId: Long = objectMapper.readValue(userIdString)
        chatBase.removeMember(chatId, userId)
    }

    fun editUser(userId: Long, userString: String) {
        val user: User = objectMapper.readValue(userString)
        userBase.edit(userId, user)
    }

    fun removeContact(userId: Long, contactString: String) {
        val contact: User = objectMapper.readValue(contactString)
        userBase.removeContact(userId, contact.id, contact.name)
    }

    fun addContact(userId: Long, contactString: String) {
        val contact: User = objectMapper.readValue(contactString)
        userBase.addContact(userId, contact.id, contact.name)
    }




    fun sendMessage(messageString : String) {
        val message : Message = objectMapper.readValue(messageString)
        messageBase.add(message)
    }

    fun editMessage(messageId: Long, messageString: String) {
        val message: Message = objectMapper.readValue(messageString)
        messageBase.edit(messageId, message)
    }

    fun deleteMessage(messageId: Long) {
        messageBase.remove(messageId)
    }

    fun getMessage(messageId: Long): Message? {
        return messageBase.get(messageId)
    }




    fun addChat(chatString: String) {
        val chat: Chat = objectMapper.readValue(chatString)
        chatBase.add(chat)
    }

    fun getChat(chatId: Long): Chat? {
        return chatBase.get(chatId)
    }

    fun getMessages(chatId: Long): MutableSet<Message>{
        return chatBase.getMessages(chatId)
    }

    fun getMembers(chatId: Long): MutableList<Long> {
        return chatBase.getMembers(chatId)
    }

    fun getAdmins(chatId: Long): MutableSet<Long> {
        return chatBase.getAdmins(chatId)
    }

    fun deleteChat(chatId: Long) {
        chatBase.remove(chatId)
    }

    fun editChat(chatId: Long, chatString: String) {
        val chat: Chat = objectMapper.readValue(chatString)
        chatBase.edit(chatId, chat)
    }

    fun addMember(chatId: Long, memberIdString: String) {
        val memberId: Long = objectMapper.readValue(memberIdString)
        chatBase.addMember(chatId, memberId)
    }

    fun kickMember(chatId: Long, memberIdString: String) {
        val memberId: Long = objectMapper.readValue(memberIdString)
        chatBase.removeMember(chatId, memberId)
    }

    fun addAdmin(chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        chatBase.addAdmin(chatId, adminId)
    }

    fun removeAdmin(chatId: Long, adminIdString: String) {
        val adminId: Long = objectMapper.readValue(adminIdString)
        chatBase.removeAdmin(chatId, adminId)
    }




    fun register(loginDataString: String) : LoginData {
        val userLoginData: LoginData = objectMapper.readValue(loginDataString)
        passwordBase.add(userLoginData.login, userLoginData.password) ?: TODO() //throw loginAlreadyExistsException
        val userId = userBase.add(userLoginData.user)
        userLoginData.user.id = userId
        userLoginData.id = userId
        userLoginData.jwt = TokenHandler().makeToken(userLoginData.user)
        return userLoginData
    }

    fun login(loginDataString : String) : LoginData {
        val userLoginData : LoginData = objectMapper.readValue(loginDataString)
        if (passwordBase.checkPassword(userLoginData.login, userLoginData.password)) {
            val user : User = userBase.findByLogin(userLoginData.login);
            userLoginData.jwt = TokenHandler().makeToken(userBase.get(user.id) ?: throw TODO()) //throw UserNotFoundException
            userLoginData.id = user.id
        }
        return userLoginData
    }

    fun editContact(userId: Long, contactString: String) {
        val newContact: Pair<Long, String> = objectMapper.readValue(contactString)
        userBase.editContact(userId, newContact.first, newContact.second)
    }

    fun blockUser(userId: Long, blockedUserIdString: String) {
        val blockedUserId: Long = objectMapper.readValue(blockedUserIdString)
        userBase.blockUser(userId, blockedUserId)
    }

    fun unblockUser(userId: Long, userToUnlockIdString: String) {
        val userToUnlockId: Long = objectMapper.readValue(userToUnlockIdString)
        userBase.unblockUser(userId, userToUnlockId)
    }

    fun getBlockedUsers(userId: Long): MutableSet<Long> {
        return userBase.getBlocked(userId)
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