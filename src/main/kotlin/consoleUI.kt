import kotlin.system.exitProcess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun getId(): Long {
    return Client.getLoggedUserId()
}

fun getName(id: Long = getId()): String {
    return Client.UserData(id).getName()
}

fun getLogin(id: Long = getId()): String {
    return Client.UserData(id).getLogin()
}

object OptionsIO {
    private fun printOptions(options: List<String>) {
        options.mapIndexed { index, s -> println("> $index -- $s") }
    }

    private fun optionNumReader(string: String?, maxRange: Int): Int {
        if (string == null) {
            return -1
        }
        val x: Int? = string.toIntOrNull()
        if (x == null || x <= -1 || x >= maxRange) {
            return -1
        }
        return x
    }

    fun init(options: List<String>): Int {
        printOptions(options)
        var optionNum = -1
        while (optionNum == -1) {
            optionNum = optionNumReader(readLine(), options.size)
            if (optionNum == -1) {
                println("Invalid input format, please try again:")
            }
        }
        return optionNum
    }
}

fun browserExit() {
    exitProcess(0)
}

class MainMenu {
    fun mainAction() {

        val optionsList = listOf(
            "View my profile",
            "View my contacts",
            "View my chats",
            "Sign out",
            "Exit"
        )

        while (true) {
            println("Your are in main menu")
            when (OptionsIO.init(optionsList)) {
                0 -> ProfileMenu().mainAction()
                1 -> ContactsMenu().mainAction()
                2 -> ChatsListMenu().mainAction()
                3 -> LoginMenu().mainAction()
                4 -> browserExit()
            }
        }
    }
}

class LoginMenu {
    fun mainAction() {
        println("Welcome to SnailMail!")
        println("Please, sign in with your login or sign up:")
        when (OptionsIO.init(listOf("Sign in", "Sign up", "Exit"))) {
            0 -> signInAction()
            1 -> signUpAction()
            2 -> browserExit()
        }
    }

    private fun signInAction() {
        println("Sori ne podvezli")
    }

    private fun signUpAction() {
        println("Enter your login:")
        val login = readLine()
        println("Enter your name:")
        val name = readLine()
        Client.registerUser(login = login!!, name = name!!)
    }
}

class ProfileMenu {
    fun mainAction() {

        val optionsList = listOf(
            "Change name",
            "Change email",
            "View my blacklist",
            "Return"
        )

        while (true) {
            println("Your are in your profile menu")
            println("Your ID: ${getId()}")
            println("Your login: ${getLogin()}")
            println("Your name: ${getName()}")

            when (OptionsIO.init(optionsList)) {
                0 -> changeNameAction()
                1 -> changeEmailAction()
                2 -> BlockedUsersMenu().mainAction()
                3 -> return
            }
        }
    }

    private fun changeNameAction() {
        println("Enter your name:")
        Client.UserData(getId()).changeName(readLine()!!)
    }

    private fun changeEmailAction() {
        println("Enter your email:")
        Client.UserData(getId()).changeName(readLine()!!)
    }
}

class ContactsMenu {
    private fun contacts() = Client.UserData(getId()).getContacts()

    fun mainAction() {

        val options = listOf(
            "Show your contacts",
            "Add new contact",
            "Remove contact",
            "Change contact name",
            "Return"
        )
        while (true) {
            println("Your are in your contacts menu")
            when (OptionsIO.init(options)) {
                0 -> showContactsAction()
                1 -> addNewContactAction()
                2 -> removeContactAction()
                3 -> changeContactNameAction()
                4 -> return
            }
        }

    }

    private fun contactFormat(contact: Map.Entry<Long, String>): String = "ID: ${contact.key}, name: ${contact.value}"

    private fun showContactsAction() = contacts().map {
        println(contactFormat(it))
    }

    private fun addNewContactAction() {
        TODO("Required: Login DB. Must be adding new chat to this User and newContactUser")
    }

    // I suppose code below needs refactoring  --aokiga

    private fun removeContactAction() {
        println("Select contact to remove:")
        val numId = contacts().keys.toList()[OptionsIO.init(contacts().map { contactFormat(it) })]
        Client.UserData(getId()).deleteContact(numId)
    }

    private fun changeContactNameAction() {
        println("Select contact to change name:")
        val numId = contacts().keys.toList()[OptionsIO.init(contacts().map { contactFormat(it) })]
        println("Input new name for contact ${getName(numId)}:")
        Client.UserData(getId()).changeContact(numId, readLine()!!)
    }

    private fun addToBlacklist() {
        println("Select contact to add to blacklist:")
        val numId = contacts().keys.toList()[OptionsIO.init(contacts().map { contactFormat(it) })]
        Client.UserData(getId()).addBlockedUser(numId)
    }
}

class ChatsListMenu {
    private fun chats() = Client.UserData(getId()).getChats()

    fun mainAction() {
        val options = listOf(
            "Open chat",
            "Show your chats",
            "Add new chat",
            "Return"
        )
        while (true) {
            println("This is a list of your chats")
            when (OptionsIO.init(options)) {
                0 -> openChatAction()
                1 -> showChatsAction()
                2 -> createChatAction()
                5 -> return
            }
        }
    }

    private fun chatFormat(chat: Map.Entry<Long, String>): String = "ID: ${chat.key}, name: ${chat.value}"

    private fun openChatAction() {
        println("Enter chat id")
        val chatId = readLine()!!.toLong()
        if (chatId in chats().keys) {
            ChatMenu(chatId).mainAction()
        } else {
            println("Sorry, you don't have this chat in your chat list")
        }
    }

    private fun showChatsAction() = chats().map {
        println(chatFormat(it))
    }

    private fun createChatAction() {
        TODO("Must be adding new chat and adding members")
    }
}

class ChatMenu(private val chatId : Long) {

    fun mainAction() {

        val options = listOf(
            "Send message",
            "Show latest messages",
            "Add new user",
            "Change chat's name",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${Client.ChatData(chatId).getName()} chat")
            when (OptionsIO.init(options)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> addUserAction()
                3 -> changeChatNameAction()
                4 -> {
                    leaveChatAction()
                    return
                }
                5 -> return
            }
        }
    }

    private fun sendMessageAction() {
        println("Enter your message")
        val messageId = Client.MessageData().createMessage(readLine()!!, chatId, getId())
        Client.ChatData(chatId).sendMessage(messageId)
    }

    private fun showMessagesAction() {
        TODO("Don't know how to get messages")
    }

    private fun addUserAction() {
        TODO("Check existence user in chat")
        /*
        println("Enter id of user")
        Client.ChatData(chatId).addUser(readLine()!!.toLong())
        */
    }

    private fun changeChatNameAction() {
        println("Input new name for chat")
        Client.ChatData(chatId).changeChatName(readLine()!!)
    }

    private fun leaveChatAction() = Client.UserData(getId()).deleteChat(chatId)
}

class BlockedUsersMenu {
    private fun blockedUsers() = Client.UserData(getId()).getBlockedUsers()

    fun mainAction() {
        val options = listOf(
            "Show your blocked users",
            "Add user to blacklist",
            "Remove user from blacklist",
            "Return"
        )
        while (true) {
            println("Your are in your blacklist")
            when (OptionsIO.init(options)) {
                0 -> showBlockedUsersAction()
                1 -> addUserAction()
                2 -> removeUserAction()
                3 -> return
            }
        }
    }

    private fun blockedUserFormat(blockedUser: Long): String = "ID: ${blockedUser}, name: ${getName(blockedUser)}"

    private fun showBlockedUsersAction() = blockedUsers().map {
        println(blockedUserFormat(it))
    }

    private fun addUserAction() {
        TODO("Check existence of user")
        /*
        println("Enter id of user")
        Client.UserData(getId()).addBBlockedUser(numId)
        */
    }

    private fun removeUserAction() {
        println("Select user to remove:")
        val numId = blockedUsers().toList()[OptionsIO.init(blockedUsers().map {
            blockedUserFormat(it)
        })]
        Client.UserData(getId()).deleteBlockedUser(numId)
    }
}

fun main() {
    GlobalScope.launch {
        Client.webClient.run()
    }
    LoginMenu().mainAction()
    MainMenu().mainAction()
}