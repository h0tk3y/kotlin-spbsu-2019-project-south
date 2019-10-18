class UserBase(val data_path: String = ""): DataBase{
    override var baseSize: Long = 0
    private val users = mutableMapOf<Long, User>()

    init {
        // TODO: Open file {data_path} or create new data_base on this path
    }

    fun add(user : User): Long {
        users.put(baseSize, user)
        return baseSize++
    }

    fun get(id: Long): User? {
        return users[id]
    }

    fun edit(id : Long, edited_user: User) {
        if (!users.containsKey(id))
            return
        users[id] = edited_user;
    }

    fun remove(id: Long) {
        users.remove(id)
    }

}