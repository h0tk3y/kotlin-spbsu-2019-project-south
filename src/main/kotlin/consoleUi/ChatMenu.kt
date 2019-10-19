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
            println("Your are in ${Client.ChatData(chatId).getName()} chat")
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
        val messageId = Client.MessageData().createMessage(readLine()!!, chatId, getId())
        Client.ChatData(chatId).sendMessage(messageId)
    }

    private fun showMessagesAction() {
        TODO("Don't know how to get messages")
    }

    private fun addUserAction() {
        TODO("Check existence user in chat")
        /*
        println("Enter id of user")
        Client.ChatData(chatId).addUser(readLine()!!.toLong())
        */
    }

    private fun changeChatNameAction() {
        println("Input new name for chat")
        Client.ChatData(chatId).changeChatName(readLine()!!)
    }

    private fun leaveChatAction() = Client.UserData(getId()).deleteChat(chatId)
}
