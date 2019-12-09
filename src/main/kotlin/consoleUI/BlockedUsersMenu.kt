package consoleUI

import client.Client as client
import client.ServerException

// TODO = resolve conflict about blockedUsers()

class BlockedUsersMenu {
    private fun blockedUsers() = client.UserDataHandler(getId()).getBlockedUsers()

    private fun blockedUserFormat(blockedUser: Long): String = "ID: ${blockedUser}, name: ${getName(blockedUser)}"

    private fun showBlockedUsersAction() = blockedUsers().map {
        println(blockedUserFormat(it))
    }

    fun mainAction() {
        val options = listOf(
            "Show your blocked users",
            "Add user to blacklist",
            "Remove user from blacklist",
            "Return"
        )
        while (true) {
            println("You are in your blacklist")
            when (optionsIO(options)) {
                0 -> showBlockedUsersAction()
                1 -> addUserAction()
                2 -> removeUserAction()
                3 -> return
            }
        }
    }

    private fun addUserAction() {
        try {
            println("Enter id of user")
            client.UserDataHandler(getId()).blockUser(readUserId())
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun removeUserAction() {
        try {
            println("Select user to remove:")
            val numId = blockedUsers().toList()[optionsIO(blockedUsers().map {
                blockedUserFormat(it)
            })]
            client.UserDataHandler(getId()).unblockUser(numId)
        }
        catch(e : ServerException) { printException(e) }
    }
}