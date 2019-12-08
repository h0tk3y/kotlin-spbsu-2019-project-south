package consoleUI

import kotlin.system.exitProcess

fun browserExit() {
    exitProcess(0)
}

fun getId(): Long {
    return client.getLoggedUserId()
}

fun getName(id: Long = getId()): String {
    return client.UserDataHandler(id).getName()
}

fun getLogin(id: Long = getId()): String {
    return client.UserDataHandler(id).getLogin()
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
            when (optionsIO(optionsList)) {
                0 -> ProfileMenu().mainAction()
                1 -> ContactsMenu().mainAction()
                2 -> ChatsListMenu().mainAction()
                3 -> LoginMenu().mainAction()
                4 -> browserExit()
            }
        }
    }
}