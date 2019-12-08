package consoleUI

import client.Client as client
import client.ServerException

class ChatMenu(private val chatId : Long) {

    private fun isAdmin(id : Long) : Boolean = false // TODO()

    private fun members() = client.ChatDataHandler(chatId).getMembers()

    fun mainAction() {
        //TODO() - waiting for isAdmin()
        if (isAdmin(getId())) mainActionAdmin()
        else mainActionRegular()
    }

    private fun mainActionRegular() {
        val options = listOf(
            "Send message",
            "Show latest messages",
            "Show members",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${client.ChatDataHandler(chatId).getName()} chat")
            when (optionsIO(options)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> showMembersAction()
                3 -> {
                    leaveChatAction()
                    return
                }
                4 -> return
            }
        }
    }

    private fun mainActionAdmin() {
        val options = listOf(
            "Send message",
            "Show latest messages",
            "Show member",
            "Add new user",
            "Kick user",
            "Change chat's name",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${client.ChatDataHandler(chatId).getName()} chat")
            when (optionsIO(options)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> showMembersAction()
                3 -> addMemberAction()
                4 -> kickMemberAction()
                5 -> changeChatNameAction()
                6 -> {
                    leaveChatAction()
                    return
                }
                7 -> return
            }
        }
    }

    private fun sendMessageAction() {
        try {
            println("Enter your message")
            client.ChatDataHandler(chatId).sendMessage(readNotEmptyLine())
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun showMessagesAction() { // Maybe needs refactoring
        val lim = 25
        val options = listOf(
            "Continue",
            "Return"
        )
        try {
            val messages = client.ChatDataHandler(chatId).getMessages()
            var i = 0
            while (i < messages.size) {
                for (cnt in 0 until lim) {
                    if (i == messages.size) return
                    /*
                    val userId = client.MessageDataHandler(messages[i]).getUserId()
                    val userName = client.UserDataHandler(userId).getName()
                    val text = client.MessageDataHandler(messages[i]).getText()
                    println("From $userName: $text")
                    if (userId != getId()) client.MessageDataHandler(messages[i]).readMessage()
                    */

                    val messageId = messages[i].id
                    val userId = messages[i].userId
                    val userName = client.UserDataHandler(userId).getName()
                    val text = messages[i].text
                    if (userId !in client.UserDataHandler(getId()).getBlockedUsers() && !messages[i].isDeleted) {
                        println("$messageId\n$userName\n$text\n")
                    }
                    if (userId != getId()) client.MessageDataHandler(messageId).readMessage()

                    ++i
                }
                if (optionsIO(options) == 1) return
            }
        } catch (e: ServerException) {
            printException(e)
        }
    }

    private fun addMemberAction() {
        try {
            println("Enter id of user")
            val numId = readId()
            client.ChatDataHandler(chatId).addMember(numId)
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun changeChatNameAction() {
        try {
            println("Input new name for chat")
            client.ChatDataHandler(chatId).changeChatName(readNotEmptyLine())
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun leaveChatAction() {
        try {
            //TODO() - need delete chat action
            //client.UserDataHandler(getId()).deleteChat(chatId)
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun memberFormat(memberId: Long): String =
        "ID: ${memberId}, Name: ${client.UserDataHandler(memberId).getName()}, isAdmin: ${isAdmin(memberId)}"

    private fun showMembersAction() {
        members().map {
            println(memberFormat(it))
        }
    }

    private fun kickMemberAction() {
        val id : Long
        while (true) {
            println("Select member to kick:")
            val numId = members()[optionsIO(members().map { memberFormat(it) })]
            if (isAdmin(numId)) {
                println("You cannot kick admin")
                continue
            }
            id = numId
            break
        }
        client.ChatDataHandler(chatId).kickMember(id)
    }
}