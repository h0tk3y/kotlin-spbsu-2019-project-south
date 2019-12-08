package consoleUI

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

            when (optionsIO(optionsList)) {
                0 -> changeNameAction()
                1 -> changeEmailAction()
                2 -> BlockedUsersMenu().mainAction()
                3 -> return
            }
        }
    }

    private fun changeNameAction() {
        println("Enter your name:")
        client.UserDataHandler(getId()).changeName(readLine()!!)
    }

    private fun changeEmailAction() {
        println("Enter your email:")
        client.UserDataHandler(getId()).changeEmail(readLine()!!)
    }
}
