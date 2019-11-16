
import DataBases.DataBaseHandler
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class DataBaseTests {

    val lovelyTestString = "Hello, Andrey!"

    @Test
    fun `1 + 1 = 2`() {
        assertEquals(2, 1 + 1, "1 + 1 should equal 2")
    }

    @Test
    fun addToMap() {
        val map = mutableMapOf<Long, String>();
        map.put(1, lovelyTestString)
        assert(map.containsValue(lovelyTestString))
        assert(map[1] == lovelyTestString)
    }

    @Test
    fun addGetUserDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        // Subtest1
        var user1 = User(-1, "Vadim Salavatov")
        val id1 = userDB.add(user1)
        assertNotNull(userDB.get(id1))
        user1 = userDB.get(id1)!!
        assert(user1.login == "Vadim Salavatov")

        // Subtest2
        var user2 = User(-1, "sp", "Ivan Pavlov")
        user2.email = "pavlov200912@mail.ru"
        val id2 = userDB.add(user2)
        assertNotNull(userDB.get(id2))
        user2 = userDB.get(id2)!!
        assert(user2.login == "sp")
        assert(user2.name == "Ivan Pavlov")
        assert(user2.email == "pavlov200912@mail.ru")


        userDB.remove(id1)
        userDB.remove(id2)
        handler.closeAll()
    }

    @Test
    fun addRemoveGetUserDB() {
        val handler = DataBaseHandler()
        handler.initAll()

        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "Vadim Salavatov")
        val id1 = userDB.add(user1)
        userDB.remove(id1)
        assertNull(userDB.get(id1))

        handler.closeAll()
    }

    @Test
    fun addEditGetUserDB() {

        val handler = DataBaseHandler()
        handler.initAll()

        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "Vadim Salavatov")
        val id1 = userDB.add(user1)

        val editedUser = User(id1, "vadimka123", "Vadim Salavatov")
        userDB.edit(id1, editedUser)
        assert(userDB.get(id1)!!.login == "Vadim Salavatov") // You can't edit login!
        assert(userDB.get(id1)!!.name == "Vadim Salavatov")

        userDB.remove(id1)
        handler.closeAll()
    }

    @Test
    fun addGetUserContacts() {

        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val contactUser = User(-1, "sp", "Ivan Pavlov")
        contactUser.email = "pavlov200912@mail.ru"
        val contactId = userDB.add(contactUser)

        val mainUser = User(-1, "Vadim Salavatov")
        mainUser.contacts.put(contactId, "My fng neighbour")// TODO? UNIQUE PAIR (user, other)
        val userId = userDB.add(mainUser)

        val contacts = userDB.getContacts(userId)
        assert(contacts[contactId] == "My fng neighbour")

        userDB.remove(contactId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun addGetContacts() {
        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val contactUser = User(-1, "sp", "Ivan Pavlov")
        contactUser.email = "pavlov200912@mail.ru"
        val contactId = userDB.add(contactUser)

        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.addContact(userId, contactId, "My fng neighbour")

        val contacts = userDB.getContacts(userId)
        assert(contacts[contactId] == "My fng neighbour")


        userDB.remove(contactId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun addEditGetContats() {
        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val contactUser = User(-1, "sp", "Ivan Pavlov")
        contactUser.email = "pavlov200912@mail.ru"
        val contactId = userDB.add(contactUser)

        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.addContact(userId, contactId, "My fng neighbour")
        userDB.editContact(userId, contactId, "My lovely neighbour")

        val contacts = userDB.getContacts(userId)
        assert(contacts[contactId] == "My lovely neighbour")


        userDB.remove(contactId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun addGetRemoveContacts() {
        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val contactUser = User(-1, "sp", "Ivan Pavlov")
        val contactId = userDB.add(contactUser)

        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.addContact(userId, contactId, "My fng neighbour")
        userDB.removeContact(userId, contactId, "My fng neighbour")

        val contacts = userDB.getContacts(userId)
        assert(contacts[contactId] != "My fng neighbour")


        userDB.remove(contactId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun blockUser() {
        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val blockedUser = User(-1, "sp", "Ivan Pavlov")
        val blockedId = userDB.add(blockedUser)

        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.blockUser(userId, blockedId)
        assert(userDB.isBlocked(userId, blockedId))


        userDB.remove(blockedId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun unblockUser() {

        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val blockedUser = User(-1, "sp", "Ivan Pavlov")
        val blockedId = userDB.add(blockedUser)

        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.blockUser(userId, blockedId)
        userDB.unblockUser(userId, blockedId)
        assert(!userDB.isBlocked(userId, blockedId))


        userDB.remove(blockedId)
        userDB.remove(userId)
        handler.closeAll()
    }

    @Test
    fun getBlockedUsers() {

        val handler = DataBaseHandler()
        handler.initAll()
        val userDB = UserBase(handler.connection!!)

        val blockedUser = User(-1, "sp", "Ivan Pavlov")
        val blockedId = userDB.add(blockedUser)

        val otherBlockedUser = User(-1, "sp", "Ivan Pavlov")
        val otherblockedId = userDB.add(otherBlockedUser)


        val mainUser = User(-1, "Vadim Salavatov")
        val userId = userDB.add(mainUser)

        userDB.blockUser(userId, otherblockedId)
        userDB.blockUser(userId, blockedId)

        val blocks = userDB.getBlocked(userId)
        assert(blocks.contains(blockedId))
        assert(blocks.contains(otherblockedId))
        assert(blocks.size == 2)

        userDB.remove(blockedId)
        userDB.remove(userId)
        userDB.remove(otherblockedId)
        handler.closeAll()
    }


    @Test
    fun addGetChatDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)

        var chat1 = Chat(-1, true, "Vadim chat")
        val id1 = chatDB.add(chat1)
        assertNotNull(chatDB.get(id1))
        chat1 = chatDB.get(id1)!!
        assert(chat1.name == "Vadim chat")

        chatDB.remove(id1)
        handler.closeAll()
    }

    @Test
    fun removeChatDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)

        val chat1 = Chat(-1, true, "Vadim chat")
        val id1 = chatDB.add(chat1)
        assertNotNull(chatDB.get(id1))
        chatDB.remove(id1)
        assertNull(chatDB.get(id1))

        handler.closeAll()
    }

    @Test
    fun editChatDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)

        var chat1 = Chat(-1, true, "Vadim chat")
        val id1 = chatDB.add(chat1)
        assertNotNull(chatDB.get(id1))
        chat1 = Chat(id1, true, "Vadimich chat")
        chatDB.edit(id1, chat1)
        assert(chatDB.get(id1)!!.name == "Vadimich chat")

        chatDB.remove(id1)
        handler.closeAll()
    }

    @Test
    fun getMembersChatDB() {

        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)
        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "sp", "Ivan Pavlov")
        val user2 = User(-1, "vs", "Vadim Salavatov")
        val user1Id = userDB.add(user1)
        val user2Id = userDB.add(user2)
        val chat1 = Chat(-1, true, "Vadim chat")
        val chatId = chatDB.add(chat1)
        chatDB.addMember(chatId, user1Id)
        chatDB.addAdmin(chatId, user2Id)
        val members = chatDB.getMembers(chatId)
        assert(members.size == 2)
        assert(members.contains(user1Id))
        assert(members.contains(user2Id))

        userDB.remove(user1Id)
        userDB.remove(user2Id)
        chatDB.remove(chatId)
        handler.closeAll()
    }

    @Test
    fun getAdminsChatDB() {

        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)
        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "sp", "Ivan Pavlov")
        val user2 = User(-1, "vs", "Vadim Salavatov")
        val user1Id = userDB.add(user1)
        val user2Id = userDB.add(user2)
        val chat1 = Chat(-1, true, "Vadim chat")
        val chatId = chatDB.add(chat1)
        chatDB.addMember(chatId, user1Id)
        chatDB.addAdmin(chatId, user2Id)
        val admins = chatDB.getAdmins(chatId)
        assert(admins.size == 1)
        assert(admins.contains(user2Id))

        userDB.remove(user1Id)
        userDB.remove(user2Id)
        chatDB.remove(chatId)
        handler.closeAll()
    }

    @Test
    fun removeAdminChatDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)
        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "sp", "Ivan Pavlov")
        val user2 = User(-1, "vs", "Vadim Salavatov")
        val user1Id = userDB.add(user1)
        val user2Id = userDB.add(user2)
        val chat1 = Chat(-1, true, "Vadim chat")
        val chatId = chatDB.add(chat1)
        chatDB.addMember(chatId, user1Id)
        chatDB.addAdmin(chatId, user2Id)
        chatDB.addAdmin(chatId, user1Id)
        chatDB.removeAdmin(chatId, user2Id)
        assert(!chatDB.getAdmins(chatId).contains(user2Id))

        userDB.remove(user1Id)
        userDB.remove(user2Id)
        chatDB.remove(chatId)
        handler.closeAll()
    }

    @Test
    fun removeMemberChatDB() {
        val handler = DataBaseHandler()
        handler.initAll()
        val chatDB = ChatBase(handler.connection!!)
        val userDB = UserBase(handler.connection!!)

        val user1 = User(-1, "sp", "Ivan Pavlov")
        val user2 = User(-1, "vs", "Vadim Salavatov")
        val user1Id = userDB.add(user1)
        val user2Id = userDB.add(user2)
        val chat1 = Chat(-1, true, "Vadim chat")
        val chatId = chatDB.add(chat1)
        chatDB.addMember(chatId, user1Id)
        chatDB.addAdmin(chatId, user2Id)
        chatDB.removeMember(chatId, user1Id)
        val members1 = chatDB.getMembers(chatId)
        assert(members1.size == 1)
        assert(members1.contains(user2Id))
        chatDB.removeMember(chatId, user2Id)
        assert(chatDB.getMembers(chatId).isEmpty())

        userDB.remove(user1Id)
        userDB.remove(user2Id)
        chatDB.remove(chatId)
        handler.closeAll()
    }
    /* @Test
     fun addGetMessageDB() {
         val messageDB = MessageBase(connection )
         var message = Message("Hello, Vadim Salavatov!", -1, -1, -1)
         val id = messageDB.add(message)
         assertNotNull(messageDB.get(id))
         message = messageDB.get(id)!!
         assert(message.text == "Hello, Vadim Salavatov!")
     }*/
}