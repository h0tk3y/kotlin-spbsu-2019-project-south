package consoleUI

import client.ServerException
import client.Client as client

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
            println("Your email: ${client.UserDataHandler().getEmail()}")
            println()

            when (optionsIO(optionsList, withReturn = true)) {
                0 -> changeNameAction()
                1 -> changeEmailAction()
                2 -> BlockedUsersMenu().mainAction()
                3 -> return
            }
        }
    }

    private fun changeNameAction() {
        println("Enter your name:")
        val newName = readNotEmptyLine()
        try {
            client.UserDataHandler(getId()).changeName(newName)
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun changeEmailAction() {
        println("Enter your email:")
        val newEmail = readNotEmptyLine()
        try {
            client.UserDataHandler(getId()).changeEmail(newEmail)
        }
        catch (e : ServerException) { printException(e) }
    }
}
