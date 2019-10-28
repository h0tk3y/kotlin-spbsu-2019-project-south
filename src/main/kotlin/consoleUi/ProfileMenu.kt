package consoleUi

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
            println("Your email: ${Client.UserData().getEmail()}")
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
        Client.UserData(getId()).changeName(readLine()!!)
    }

    private fun changeEmailAction() {
        println("Enter your email:")
        Client.UserData(getId()).changeEmail(readLine()!!)
    }
}
