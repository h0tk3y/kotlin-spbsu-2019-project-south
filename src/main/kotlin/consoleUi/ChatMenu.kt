package consoleUi

class ChatMenu(private val chatId : Long) {

    fun mainAction() {

        val options = listOf(
            "Send message",
            "Show latest messages",
            "Add new user",
            "Change chat's name",
            "Leave chat",
            "Return"
        )
        while (true) {
            println("Your are in ${Client.ChatDataHandler(chatId).getName()} chat")
            when (optionsIO(options)) {
                0 -> sendMessageAction()
                1 -> showMessagesAction()
                2 -> addUserAction()
                3 -> changeChatNameAction()
                4 -> {
                    leaveChatAction()
                    return
                }
                5 -> return
            }
        }
    }

    private fun sendMessageAction() {
        println("Enter your message")
        val messageId = Client.MessageDataHandler().createMessage(readLine()!!, chatId, getId())
        Client.ChatDataHandler(chatId).sendMessage(messageId)
    }

    private fun showMessagesAction() { // TODO("Maybe needs refactoring")
        val lim = 25
        val options = listOf(
            "Continue",
            "Return"
        )

        val messages = Client.ChatData(chatId).getMessages()
        var i = 0
        while (i < messages.size) {
            for (cnt in 0 until lim) {
                if (i == messages.size) return
                val userId = Client.MessageData(messages[i]).getUserId()
                val userName = Client.UserData(userId).getName()
                val text = Client.MessageData(messages[i]).getText()
                println("From $userName: $text")
                if (userId != getId()) Client.MessageData(messages[i]).readMessage()
                i++
            }
            if (optionsIO(options) == 1) return
        }
    }

    private fun addUserAction() {
        //TODO("Check existence user in chat")
        println("Enter id of user")
        val numId = readLine()!!.toLong()
        Client.ChatData(chatId).addUser(numId)
    }

    private fun changeChatNameAction() {
        println("Input new name for chat")
        Client.ChatDataHandler(chatId).changeChatName(readLine()!!)
    }

    private fun leaveChatAction() = Client.UserDataHandler(getId()).deleteChat(chatId)
}
