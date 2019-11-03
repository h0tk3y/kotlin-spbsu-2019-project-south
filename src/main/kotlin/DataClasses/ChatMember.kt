package DataClasses

enum class MemberStatus {
    OWNER,
    ADMIN,
    MEMBER,
}

data class ChatMember(val chatId: Long, val memberId: Long, val memberStatus: MemberStatus)