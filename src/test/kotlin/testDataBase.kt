
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

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
        val userDB = UserBase()
        var user = User(-1, "Vadim Salavatov")
        val id = userDB.add(user)
        assertNotNull(userDB.get(id))
        user = userDB.get(id)!!
        assert(user.login == "Vadim Salavatov")
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