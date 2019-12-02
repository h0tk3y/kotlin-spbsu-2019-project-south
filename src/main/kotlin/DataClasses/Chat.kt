data class Chat(
    var id: Long = -1,
    val isSingle: Boolean = true,
    val owner_id: Long = -1,
    var name: String = "New Chat",
    val members: MutableSet<Long> = mutableSetOf()
) {
    val admins: MutableSet<Long> = mutableSetOf()
}
