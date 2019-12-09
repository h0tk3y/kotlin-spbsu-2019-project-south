import client.Client
import client.Client.webClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

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
}