package consoleUI

import client.Client as client
import client.ServerException

class ChatMenu(private val chatId : Long) {

    private fun isAdmin(id : Long) : Boolean = (id in client.ChatDataHandler(chatId).getAdmins())

    private fun members() = client.ChatDataHandler(chatId).getMembers()

    fun mainAction() {
        if (isAdmin(getId())) mainActionAdmin()
        else mainActionRegular()
    }

    private fun mainActionRegular() {
        val options = listOf(
            "Send message",
            "Show latest messages",
            "Edit message",
            "Show members",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${client.ChatDataHandler(chatId).getName()} chat")
            when (optionsIO(options, withReturn = true)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> editMessageAction()
                3 -> showMembersAction()
                4 -> {
                    leaveChatAction()
                    return
                }
                5 -> return
            }
        }
    }

    private fun mainActionAdmin() {
        val options = listOf(
            "Send message",
            "Show latest messages",
            "Edit message",
            "Show member",
            "Add new user",
            "Kick user",
            "Change chat's name",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${client.ChatDataHandler(chatId).getName()} chat")
            when (optionsIO(options, withReturn = true)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> editMessageAction()
                3 -> showMembersAction()
                4 -> addMemberAction()
                5 -> kickMemberAction()
                6 -> changeChatNameAction()
                7 -> {
                    leaveChatAction()
                    return
                }
                8 -> return
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
            var i = messages.size - 1
            while (i >= 0) {
                for (cnt in 0 until lim) {
                    if (i < 0) return
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
                    if (userId !in client.UserDataHandler(getId()).getBlockedUsers() && !messages[i].deleted) {
                        println("$messageId\n$userName\n$text\n")
                    }
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
            val numId : Long
            try { numId = readUserId() }
            catch (e : IOException) {
                printException(e)
                return
            }
            client.ChatDataHandler(chatId).addMember(numId)
            // TODO  Need to add our chat to list of chats
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
            //TODO() - also we need to delete chat form chat list of every member
            //client.UserDataHandler(getId()).deleteChat(chatId)
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun memberFormat(memberId: Long): String =
        "ID: ${memberId}, Name: ${client.UserDataHandler(memberId).getName()}, isAdmin: ${isAdmin(memberId)}"

    private fun showMembersAction() {
        try {
            members().map {
                println(memberFormat(it))
            }
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun kickMemberAction() {
        try {
            val id: Long
            while (true) {
                println("Select member to kick:")
                val i = optionsIO(members().map { memberFormat(it) })
                if (i == -1) return
                val numId = members()[i]
                if (isAdmin(numId)) {
                    println("You cannot kick admin")
                    continue
                }
                id = numId
                break
            }
            client.ChatDataHandler(chatId).kickMember(id)
            // TODO() - we need to delete chat from list of chats of kicked user
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun editMessageAction() {
        println("Enter id of message")
        val id: Long
        try { id = readMessageId() }
        catch (e: IOException) {
            printException(e)
            return
        }
        if (client.MessageDataHandler(id).getChatId() != chatId) {
            println("You cannot edit messages in other chats")
            return
        }
        if (client.MessageDataHandler(id).getUserId() != getId() && !isAdmin(getId())) {
            println("You cannot edit this message")
            return
        }
        MessageMenu(id).mainAction()
    }
}