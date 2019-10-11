enum class ReqType {
    ADD {
        override fun toString(): String {
            return "ADD"
        }
    },
    GET {
        override fun toString(): String {
            return "GET"
        }
    },
    EDIT {
        override fun toString(): String {
            return "EDIT"
        }
    },
    REMOVE {
        override fun toString(): String {
            return "REMOVE"
        }
    }
}