import com.fasterxml.jackson.module.kotlin.*
import RequestType.*
import FieldType.*

class Client() {
    var loggedUserId: Long = -1
    fun registerUser(login : String, name : String = login, email : String = "") {
        loggedUserId = UserData().addUser(login, name, email)
    }

    class UserData(var userId: Long = -1) {
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
        fun deleteChar(chatId : Long) {
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
    }

    class messageData(val messageId: Long)
    class chatData(val chatId: Long)
}