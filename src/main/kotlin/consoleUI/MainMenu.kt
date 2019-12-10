package consoleUI

import client.Client as client
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
            "Profile menu",
            "Contacts menu",
            "Chats menu",
            "Sign out",
            "Exit"
        )

        while (true) {
            println("Your are in main menu")
            when (optionsIO(optionsList, withReturn = true)) {
                0 -> ProfileMenu().mainAction()
                1 -> ContactsMenu().mainAction()
                2 -> ChatsListMenu().mainAction()
                3 -> return
                4 -> browserExit()
            }
        }
    }
}