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

    //TODO : Разделить CHAT на SINGLE_CHAT и GROUP_CHAT, и соотв. запросы на сервер, DB-шки
}