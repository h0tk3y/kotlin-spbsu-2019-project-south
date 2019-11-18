package DataClasses

data class UserContact(val userId: Long, val contactId: Long){
    var name: String = "Contact"
    var dialogId : Long = -1
}