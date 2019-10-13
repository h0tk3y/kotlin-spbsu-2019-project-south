data class Chat(val id: Long, val isSingle: Boolean, val name: String, val owners: MutableSet<Long>) {
    val members: MutableSet<Long> = owners
    val messages: MutableList<Long> = mutableListOf()
}
