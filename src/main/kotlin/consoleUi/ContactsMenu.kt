package consoleUi

class ContactsMenu {
    private fun contacts() = Client.UserData(getId()).getContacts()

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

    private fun contactFormat(contact: Map.Entry<Long, String>): String = "ID: ${contact.key}, name: ${contact.value}"

    private fun showContactsAction() = contacts().map {
        println(contactFormat(it))
    }

    private fun addNewContactAction() {
        TODO("Required: Login DB. Must be adding new chat to this User and newContactUser")
    }

    // I suppose code below needs refactoring  --aokiga

    private fun removeContactAction() {
        println("Select contact to remove:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        Client.UserData(getId()).deleteContact(numId)
    }

    private fun changeContactNameAction() {
        println("Select contact to change name:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        println("Input new name for contact ${getName(numId)}:")
        Client.UserData(getId()).changeContact(numId, readLine()!!)
    }

    private fun addToBlacklist() {
        println("Select contact to add to blacklist:")
        val numId = contacts().keys.toList()[optionsIO(contacts().map { contactFormat(it) })]
        Client.UserData(getId()).addBlockedUser(numId)
    }
}