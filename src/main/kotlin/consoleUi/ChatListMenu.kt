package consoleUi

class ChatsListMenu {
    private fun chats() = Client.UserData(getId()).getChats()

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
                5 -> return
            }
        }
    }

    private fun chatFormat(chat: Map.Entry<Long, String>): String = "ID: ${chat.key}, name: ${chat.value}"

    private fun openChatAction() {
        println("Select chat to open:")
        val numId = chats().keys.toList()[optionsIO(chats().map { chatFormat(it) })]
        Client.UserData(getId()).deleteContact(numId)
    }

    private fun showChatsAction() = chats().map {
        println(chatFormat(it))
    }

    private fun createChatAction() {
        TODO("Must be adding new chat and adding members")
    }
}