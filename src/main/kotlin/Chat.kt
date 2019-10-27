data class Chat(val id: Long = -1, val isSingle: Boolean = true, var name: String = "New Chat", val owners: MutableSet<Long> = mutableSetOf()) {
    val members: MutableSet<Long> = owners
    val messages: MutableList<Long> = mutableListOf()
}
