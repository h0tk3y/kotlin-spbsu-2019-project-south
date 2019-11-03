package consoleUi

class BlockedUsersMenu {
    private fun blockedUsers() = Client.UserDataHandler(getId()).getBlockedUsers()

    fun mainAction() {
        val options = listOf(
            "Show your blocked users",
            "Add user to blacklist",
            "Remove user from blacklist",
            "Return"
        )
        while (true) {
            println("Your are in your blacklist")
            when (optionsIO(options)) {
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
        val numId = blockedUsers().toList()[optionsIO(blockedUsers().map {
            blockedUserFormat(it)
        })]
        Client.UserDataHandler(getId()).unblockUser(numId)
    }
}