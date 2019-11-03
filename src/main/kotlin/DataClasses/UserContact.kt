package DataClasses

enum class ContactStatus{
    CONTACT,
    BLOCK,
    BLOCKED,
}

data class UserContact(val userId: Long, val contactId: Long, var contactStatus: ContactStatus = ContactStatus.CONTACT){
    var name: String = "Contact"
    var dialogId : Long = -1
}