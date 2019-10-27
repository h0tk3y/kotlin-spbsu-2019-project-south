data class User(var id: Long) {
    var login: String = ""
    var name: String = ""
    var email: String = ""
    val blockedUsers: MutableSet<Long> = mutableSetOf()
    val chatsId: MutableMap<Long, String> = mutableMapOf()
    val contacts: MutableMap<Long, String> = mutableMapOf()
}