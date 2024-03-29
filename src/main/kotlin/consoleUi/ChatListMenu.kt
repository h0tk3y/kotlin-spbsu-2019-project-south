package consoleUi

class ChatsListMenu {
    private fun chats() = Client.UserDataHandler(getId()).getChats()

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

    private fun selectNameForSingleChat(chatId : Long) : String {
        val members = Client.ChatData(chatId).getMembers()
        val userId = if (getId() == members.first()) {
            members.last()
        } else {
            members.first()
        }
        val contacts = Client.UserData(getId()).getContacts()
        if (userId in contacts)
            return contacts[userId]!!
        else
            return Client.UserData(userId).getLogin()
    }

    private fun chatFormat(chat: Map.Entry<Long, String>): String =
        if (!Client.ChatData(chat.key).isSingle()) {
            "ID: ${chat.key}, name: ${chat.value}"
        } else {
            "ID: ${chat.key}, name: ${selectNameForSingleChat(chat.key)}"
        }

    private fun openChatAction() {
        println("Select chat to open:")
        val numId = chats().keys.toList()[optionsIO(chats().map { chatFormat(it) })]
        ChatMenu(numId).mainAction()
    }

    private fun showChatsAction() = chats().map {
        println(chatFormat(it))
    }

    private fun createChatAction() {
        //TODO("Allow to add users to chat")
        println("Input name of chat")
        val chatId = Client.ChatData().createChat(false, readLine()!!, mutableSetOf(getId()))
        ChatMenu(chatId).mainAction()
    }
}