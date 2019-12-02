data class User(var id: Long, val login: String = "", var name : String = login) {
    var email: String = ""
    val blockedUsers: MutableSet<Long> = mutableSetOf()
    val chatsId: MutableMap<Long, String> = mutableMapOf()
    val contacts: MutableMap<Long, String> = mutableMapOf()
}