package consoleUi

class ContactsMenu {
    private fun contacts() = Client.UserDataHandler(getId()).getContacts()

    fun mainAction() {

        val options = listOf(
            "Show your contacts",
            "Add new contact",
            "Remove contact",
            "Change contact name",
            "Return"
        )
        while (true) {
            println("Your are in your contacts menu")
            when (optionsIO(options)) {
                0 -> showContactsAction()
                1 -> addNewContactAction()
                2 -> removeContactAction()
                3 -> changeContactNameAction()
                4 -> return
            }
        }

    }

    private fun contactFormat(contact: Map.Entry<Long, String>): String =
        "ID: ${contact.key}, name: ${contact.value}"

    private fun showContactsAction() = contacts().map {
        println(contactFormat(it))
    }

    private fun addNewContactAction() {
        println("Input id of User")
        val userId = readLine()!!.toLong()
        println("Input new name for contact")
        var userName = readLine()
        if (userName.isNullOrEmpty()) userName = Client.UserData(userId).getName()
        Client.UserData(getId()).addContact(userId, userName)
        Client.UserData(userId).addContact(getId(), getName())
        Client.ChatData().createChat(true, "", mutableSetOf(getId(), userId))
        //TODO("Required: Login DB. Must be adding new chat to this User and newContactUser")
    }

    private fun removeContactAction() {
        println("Select contact to remove:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        Client.UserDataHandler(getId()).removeContact(numId)
    }

    private fun changeContactNameAction() {
        println("Select contact to change name:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        println("Input new name for contact ${getName(numId)}:")
        Client.UserDataHandler(getId()).changeContactName(numId, readLine()!!)
    }

    private fun addToBlacklist() {
        println("Select contact to add to blacklist:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        Client.UserDataHandler(getId()).blockUser(numId)
    }
}