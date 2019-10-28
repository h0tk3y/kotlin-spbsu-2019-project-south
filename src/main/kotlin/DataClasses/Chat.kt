data class Chat(var id: Long = -1, val isSingle: Boolean = true, var name: String = "New Chat", val members: MutableSet<Long> = mutableSetOf()) {
    val owners: MutableSet<Long> = members
    val messages: MutableList<Long> = mutableListOf()
}
