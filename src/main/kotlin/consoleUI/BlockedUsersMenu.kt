package consoleUI

import client.Client as client
import client.ServerException

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
            when (optionsIO(options, withReturn = true)) {
                0 -> showBlockedUsersAction()
                1 -> addUserAction()
                2 -> removeUserAction()
                3 -> return
            }
        }
    }

    private fun addUserAction() {
        println("Enter id of user")
        val userId : Long
        try { userId = readUserId() }
        catch (e : IOException) {
            printException(e)
            return
        }
        try { client.UserDataHandler(getId()).blockUser(userId) }
        catch (e : ServerException) { printException(e) }
    }

    private fun removeUserAction() {
        try {
            println("Select user to remove:")
            val i = optionsIO(blockedUsers().map {
                blockedUserFormat(it)
            })
            if (i == -1) return
            val numId = blockedUsers().toList()[i]
            client.UserDataHandler(getId()).unblockUser(numId)
        }
        catch(e : ServerException) { printException(e) }
    }
}