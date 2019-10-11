enum class FieldType{
    USER {
        override fun toString(): String {
            return "USER"
        }
    },
    MESSAGE {
        override fun toString(): String {
            return "MESSAGE"
        }
    },
    CHAT {
        override fun toString(): String {
            return "CHAT"
        }
    }
}