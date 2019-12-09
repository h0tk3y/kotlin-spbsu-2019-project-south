import client.Client
import client.Client.webClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.platform.commons.util.ClassUtils
import transport.RequestType
import transport.WebClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerTests {

    var id1 : Long = -1
    var id2 : Long = -1
    var id3 : Long = -1

    init {
        GlobalScope.launch {
            Server.run()
        }
        Thread.sleep(2000L)
        GlobalScope.launch {
            Client.webClient.run()
        }
        try {
            Client.LoginDataHandler().registerUser("handmidas", "azul3", "Danila", "danila@kotlin.com")
        } catch (e: client.ServerException) {
            Client.LoginDataHandler().loginUser("handmidas", "azul3")
            assertTrue(Client.webClient.token != "")
            Client.UserDataHandler().changeName("Danila")
            Client.UserDataHandler().changeEmail("danila@kotlin.com")
        }
        id1 = Client.getLoggedUserId()
        try {
            Client.LoginDataHandler().registerUser("bosov", "antoxadelaikotlin", "Bosov", "bosov@kotlin.com")
        } catch (e: client.ServerException)
        {
            Client.LoginDataHandler().loginUser("bosov", "antoxadelaikotlin")
            assertTrue(Client.webClient.token != "")
            Client.UserDataHandler().changeName("Bosov")
            Client.UserDataHandler().changeEmail("bosov@kotlin.com")
        }
        id2 = Client.getLoggedUserId()
        try {
            Client.LoginDataHandler().registerUser("antoxa", "lollollol", "Antoxa", "antoxa@gdekotlin.com")
        } catch (e: client.ServerException) {
            Client.LoginDataHandler().loginUser("antoxa", "lollollol")
            assertTrue(Client.webClient.token != "")
            Client.UserDataHandler().changeName("Antoxa")
            Client.UserDataHandler().changeEmail("antoxa@gdekotlin.com")
        }
        id3 = Client.getLoggedUserId()
    }

    @Test
    fun testGetName() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        assertEquals("Danila", Client.UserDataHandler().getName())
    }

    @Test
    fun testGetLogin() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        assertEquals( "handmidas", Client.UserDataHandler().getLogin())
    }

    @Test
    fun testGetEmail() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        assertEquals( "danila@kotlin.com", Client.UserDataHandler().getEmail())
    }

    @Test
    fun testChangeName() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        Client.UserDataHandler().changeName("Daniil")
        val actual = Client.UserDataHandler().getName()
        Client.UserDataHandler().changeName("Danila")
        assertEquals("Daniil", actual)
    }

    @Test
    fun testChangeEmail() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        Client.UserDataHandler().changeEmail("danila@azul.com")
        val actual = Client.UserDataHandler().getEmail()
        Client.UserDataHandler().changeEmail("danila@kotlin.com")
        assertEquals("danila@azul.com", actual)
    }

    @Test
    fun testUserContacts() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        Client.UserDataHandler().addContact(id2, "Nikita Bosov")
        Client.UserDataHandler().addContact(id3, "Anton Shangareev")
        val contacts = Client.UserDataHandler().getContacts()
        assertTrue(contacts.containsKey(id2) && contacts.get(id2) == "Nikita Bosov")
        assertTrue(contacts.containsKey(id3) && contacts.get(id3) == "Anton Shangareev")
        Client.UserDataHandler().removeContact(id2, "Nikita Bosov")
        Client.UserDataHandler().removeContact(id3, "Anton Shangareev")
        val contacts2 = Client.UserDataHandler().getContacts()
        assertEquals(0, contacts2.size)
    }

    @Test
    fun testEditContact() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        Client.UserDataHandler().addContact(id2, "Nikita Bosov")
        Client.UserDataHandler().editContact(id2, "Bosov Nikita")
        val contacts = Client.UserDataHandler().getContacts()
        assertTrue(contacts.containsKey(id2) && contacts.get(id2) == "Bosov Nikita")
        Client.UserDataHandler().removeContact(id2, "Bosov Nikita")
        val contacts2 = Client.UserDataHandler().getContacts()
        assertEquals(0, contacts2.size)
    }

    @Test
    fun testBlockUser() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        Client.UserDataHandler().blockUser(id2)
        var blockedUsers = Client.UserDataHandler().getBlockedUsers()
        assertTrue(blockedUsers.contains(id2))

        Client.UserDataHandler().unblockUser(id2)
        blockedUsers = Client.UserDataHandler().getBlockedUsers()
        assertTrue(blockedUsers.size == 0)
    }

    @Test
    fun addSingleChat() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        val members = mutableSetOf<Long>()
        members.add(id1)
        members.add(id2)
        val chatId = Client.ChatDataHandler().createChat(true, "SuperChat", members)
        val chats = Client.UserDataHandler().getUserChats()
        assertTrue(chats.containsKey(chatId) && chats.get(chatId) == "SuperChat")
    }

    @Test
    fun addGroupChat() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        val members = mutableSetOf<Long>();
        members.add(id1)
        members.add(id2)
        members.add(id3)
        val chatId = Client.ChatDataHandler().createChat(false, "SuperGroupChat", members)
        val chats = Client.UserDataHandler().getUserChats()
        assertTrue(chats.containsKey(chatId) && chats.get(chatId) == "SuperGroupChat")
        Client.LoginDataHandler().loginUser("bosov", "antoxadelaikotlin")
        val chats2 = Client.UserDataHandler().getUserChats()
        assertTrue(chats.containsKey(chatId) && chats2.get(chatId) == "SuperGroupChat")
    }

    @Test
    fun addMember() {
        Client.LoginDataHandler().loginUser("handmidas", "azul3")
        assertTrue(Client.webClient.token != "")
        val members = mutableSetOf<Long>();
        members.add(id1)
        val chatId = Client.ChatDataHandler().createChat(false, "UltraGroupChat", members)
        Client.ChatDataHandler(chatId).addMember(id2)
        val chats = Client.UserDataHandler().getUserChats()
        assertTrue(chats.containsKey(chatId) && chats.get(chatId) == "UltraGroupChat")
        Client.LoginDataHandler().loginUser("bosov", "antoxadelaikotlin")
        val chats2 = Client.UserDataHandler(chatId).getUserChats()
        assertTrue(chats.containsKey(chatId) && chats.get(chatId) == "UltraGroupChat")
    }

    @Test
    fun testLoginException() {
        assertThrows<client.ServerException> {
            Client.LoginDataHandler().loginUser("handmidas", "azul4")
        }
    }

    @Test
    fun testRegisterException() {
        assertThrows<client.ServerException> {
            Client.LoginDataHandler().registerUser("handmidas", "azul4", "aaa", "bbb")
        }
    }
}