package consoleUI

import client.Client as client
import client.ServerException

class ChatsListMenu {
    private fun chats() = client.UserDataHandler(getId()).getUserChats()

    private fun selectNameForSingleChat(chatId : Long) : String {
        val members = client.ChatDataHandler(chatId).getMembers()
        val userId = if (getId() == members.first()) members.last() else members.first()
        val contacts = client.UserDataHandler(getId()).getContacts()
        return if (userId in contacts)
            contacts[userId]!!
        else
            client.UserDataHandler(userId).getName()
    }

    private fun chatFormat(chat: Map.Entry<Long, String>): String =
        if (!client.ChatDataHandler(chat.key).isSingle()) "ID: ${chat.key}, name: ${chat.value}
        else "ID: ${chat.key}, name: ${selectNameForSingleChat(chat.key)}"

    fun mainAction() {
        val options = listOf(
            "Open chat",
            "Show your chats",
            "Add new chat",
            "Return"
        )
        while (true) {
            println("This is a list of your chats")
            when (optionsIO(options)) {
                0 -> openChatAction()
                1 -> showChatsAction()
                2 -> createChatAction()
                3 -> return
            }
        }
    }

    private fun showChatsAction() {
        try {
            chats().map {
                println(chatFormat(it))
            }
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun openChatAction() {
        try {
            println("Select chat to open:")
            val numId = chats().keys.toList()[optionsIO(chats().map { chatFormat(it) })]
            ChatMenu(numId).mainAction()
        }
        catch (e : ServerException) { printException(e) }
    }


    //createChat must return chat id
    private fun createChatAction() {
        try {
            println("Input name of chat")
            val chatId = client.ChatDataHandler().createChat(false, readNotEmptyLine(), mutableSetOf(getId()))
            ChatMenu(chatId).mainAction()
        }
        catch (e : ServerException) { printException(e) }
    }
}