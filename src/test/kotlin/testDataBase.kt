
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
        assert(contacts!![contactId] == "My fng neighbour")

        handler.closeAll()
    }

    @Test
    fun addGetChatDB() {
        val chatDB = ChatBase()
        var chat = Chat(-1, true, "SingleChat", mutableSetOf())
        val id = chatDB.add(chat)
        assertNotNull(chatDB.get(id))
        chat = chatDB.get(id)!!
        assert(chat.isSingle)
        assert(chat.name == "SingleChat")
    }

    @Test
    fun addGetMessageDB() {
        val messageDB = MessageBase()
        var message = Message("Hello, Vadim Salavatov!", -1, -1, -1)
        val id = messageDB.add(message)
        assertNotNull(messageDB.get(id))
        message = messageDB.get(id)!!
        assert(message.text == "Hello, Vadim Salavatov!")
    }
}