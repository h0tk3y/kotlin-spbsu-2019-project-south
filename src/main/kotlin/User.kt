data class User(val id: Long, val login: String) {
    var name: String = ""
    var email: String = ""
    val blockedUsers: MutableSet<Long> = mutableSetOf()
    val chatsId: MutableSet<Long> = mutableSetOf() //TODO: map (id, name)
    val contacts: MutableMap<Long, String> = mutableMapOf()
}