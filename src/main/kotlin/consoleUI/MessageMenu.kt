package consoleUI

import client.Client as client

class MessageMenu(private val messageId: Long) {
    fun mainAction() {
        val options = listOf(
            "Edit message",
            "Delete message",
            "Return"
        )

        while (true) {
            when (optionsIO(options)) {
                0 -> editMessageAction()
                1 -> deleteMessagesAction()
                2 -> return
            }
        }
    }

    private fun editMessageAction() {
        println("Enter text of new message")
        val text = readNotEmptyLine()
        client.MessageDataHandler(messageId).editText(text)
    }

    private fun deleteMessagesAction() = client.MessageDataHandler(messageId).deleteMessage()
}