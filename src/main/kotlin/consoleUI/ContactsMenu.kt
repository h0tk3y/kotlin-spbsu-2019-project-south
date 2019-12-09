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
            when (optionsIO(options, withReturn = true)) {
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
            val userId : Long
            try { userId = readUserId() }
            catch (e : IOException) {
                printException(e)
                return
            }
            println("Input new name for contact")
            var userName = readLine()
            if (userName.isNullOrEmpty()) userName = client.UserDataHandler(userId).getName()
            client.UserDataHandler(getId()).addContact(userId, userName)
            client.ChatDataHandler().createChat(true, "", mutableSetOf(getId(), userId))
        } catch (e: ServerException) { printException(e) }
    }

    private fun removeContactAction() {
        try {
            println("Select contact to remove:")
            val i = optionsIO(contacts().map { contactFormat(it) })
            if (i == -1) return
            val numId = contacts().keys.toList()[i]
            val name = contacts()[numId]
            if (name != null) {
                client.UserDataHandler(getId()).removeContact(numId, name)
            }
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun changeContactNameAction() {
        try {
            println("Select contact to change name:")
            val i = optionsIO(contacts().map { contactFormat(it) })
            if (i == -1) return
            val numId = contacts().keys.toList()[i]
            println("Input new name for contact ${getName(numId)}:")
            client.UserDataHandler(getId()).editContact(numId, readNotEmptyLine())
        }
        catch (e : ServerException) { printException(e) }
    }

    private fun addToBlacklist() {
        try {
            println("Select contact to add to blacklist:")
            val i = optionsIO(contacts().map { contactFormat(it) })
            if (i == -1) return
            val numId = contacts().keys.toList()[i]
            client.UserDataHandler(getId()).blockUser(numId)
        }
        catch (e : ServerException) { printException(e) }
    }
}