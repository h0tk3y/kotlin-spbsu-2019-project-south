import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.`KotlinPackage-Extensions-6da2b0d6`.jacksonObjectMapper
import ReqType.*
import FieldType.*

class Client() {
    var loggedUserId: Long = -1

    fun registerUser(name : String, login : String, email : String) {
        val userData = UserData()
        loggedUserId = userData.addUser(login, name, email)
    }

    class UserData(var userId: Long = -1) {
        private val objectMapper = jacksonObjectMapper()
        private fun getUser(id : Long) : User {
            return objectMapper.readValue<User>(ServerRequest(GET, USER, userId).makeRequest())
        }
        private fun editUser(user : User) {
            ServerRequest(EDIT, USER, userId, objectMapper.writeValueAsString<User>(user)).makeRequest()
        }
        fun addUser(login : String, name : String, email : String) : Long {
            val newUser = User(-1, login)
            newUser.name = name
            newUser.email = email
            return objectMapper.readValue<Long>(
                ServerRequest(ADD, USER, userId, objectMapper.writeValueAsString<User>(newUser)).makeRequest())
        }
        fun addContact(contactId: Long, name : String = ""){
            val cur = getUser(userId);
            if (name == "") {
                val newUser = getUser(contactId)
                cur.contacts[contactId] = newUser.name
            }
            else {
                cur.contacts[contactId] = name
            }
            editUser(cur)
        }
        fun addBlockedUser(blockedUserId: Long){
            val cur = getUser(userId)
            cur.blockedUsers.add(blockedUserId)
            editUser(cur)
        }
        fun addChat(chatId : Long){
            val cur = getUser(userId)
            cur.chatsId.add(chatId)
            editUser(cur)
        }
        fun changeName(newName : String) {
            val cur = getUser(userId)
            cur.name = newName
            editUser(cur)
        }
    }

    class messageData(val messageId: Long)
    class chatData(val chatId: Long)
}