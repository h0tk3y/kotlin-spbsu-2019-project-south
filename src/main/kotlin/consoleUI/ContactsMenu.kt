package consoleUI

import client.Client as client
import client.ServerException

class ContactsMenu {
    private fun contacts() = client.UserDataHandler(getId()).getContacts()

    fun mainAction() {

        val options = listOf(
            "Show your contacts",
            "Add new contact",
            "Remove contact",
            "Change contact name",
            "Add contact to blacklist",
            "Return"
        )
        while (true) {
            println("Your are in your contacts menu")
            when (optionsIO(options)) {
                0 -> showContactsAction()
                1 -> addNewContactAction()
                2 -> removeContactAction()
                3 -> changeContactNameAction()
                4 -> addToBlacklist()
                5 -> return
            }
        }

    }

    private fun contactFormat(contact: Map.Entry<Long, String>): String =
        "ID: ${contact.key}, name: ${contact.value}"

    private fun showContactsAction() {
        try {
            contacts().map {
                println(contactFormat(it))
            }
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun addNewContactAction() {
        try {
            println("Input id of User")
            val userId = readUserId()
            println("Input new name for contact")
            var userName = readLine()
            if (userName.isNullOrEmpty()) userName = client.UserDataHandler(userId).getName()
            client.UserDataHandler(getId()).addContact(userId, userName)
            client.UserDataHandler(userId).addContact(getId(), getName())
            client.ChatDataHandler().createChat(true, "", mutableSetOf(getId(), userId))
        } catch (e: ServerException) { printException(e) }
    }

    private fun removeContactAction() {
        try {
            println("Select contact to remove:")
            val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
            client.UserDataHandler(getId()).removeContact(numId)
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun changeContactNameAction() {
        try {
            println("Select contact to change name:")
            val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
            println("Input new name for contact ${getName(numId)}:")
            client.UserDataHandler(getId()).editContact(numId, readNotEmptyLine())
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun addToBlacklist() {
        try {
            println("Select contact to add to blacklist:")
            val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
            client.UserDataHandler(getId()).blockUser(numId)
        }
        catch (e : ServerException) { printException(e) }
    }
}