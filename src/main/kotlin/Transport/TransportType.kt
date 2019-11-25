enum class TransportType {

    GET_USER,        //get user by id (access for all)
    GET_USER_CHATS,  //return all user chats map (ids, name) (access only for target user | ACCESS ERROR)
    REMOVE_USER,    // todo
    EDIT_USER,      //edit user data (access only for target user | ACCESS ERROR)
    ADD_CONTACT,    //add contact connection between users (access only for not blocked users |ACCESS ERROR | BL ERROR)
    EDIT_CONTACT,   //edit contact (access only for target user) (ACCESS ERROR)
    REMOVE_CONTACT, // todo
    BLOCK_USER,    // block connection (access only for target user | ACCESS ERROR | STATUS ERROR)
    UNBLOCK_USER,  // unblock connection (access only for target user | ACCESS ERROR | STATUS ERROR)
    GET_BLOCKED_USERS, // return all user blocked users map (id, name) (access only for target user | ACCESS ERROR)
    GET_CONTACTS, // return all user contacts map (id, name) (access only for target user | ACCESS ERROR)

    SEND_MESSAGE, // (access only for chat members | ACCESS ERROR)
    EDIT_MESSAGE, // (access only for author | ACCESS ERROR)
    DELETE_MESSAGE, //(access only for author and admins | ACCESS ERROR)
    GET_MESSAGE, // return message by id (access only for chat members | ACCESS ERROR)


    ADD_CHAT, // create new chat (access for all)
    GET_CHAT, // todo
    GET_MESSAGES, // return list of messages (access only for chat members | ACCESS ERROR)
    GET_MEMBERS, // return list of members id (access only for admins | ACCESS ERROR)
    GET_ADMINS, // return list of members id (access only for admins (mb for members - todo) | ACCESS ERROR)
    DELETE_CHAT, // todo
    EDIT_CHAT, // (access only for admins | ACCESS ERROR)
    ADD_MEMBER, // adding(inviting) new member to chat (hz dlya kogo access todo)
    JOIN_CHAT, // join to chat (access for not blocked in chat user | ACCESS ERROR | STATUS ERROR)
    KICK_MEMBER, // (access for admin | ACCESS ERROR | STATUS ERROR)
    LEAVE_CHAT, // (access for members | ACCESS ERROR | STATUS ERROR)
    ADD_ADMIN, // (access for owner | ACCESS ERROR | STATUS ERROR)
    REMOVE_ADMIN, // (access for owner | ACCESS ERROR | STATUS ERROR)
    BLOCK_USER_IN_CHAT, // (access for admin | ACCESS ERROR | STATUS ERROR)
    UNBLOCK_USER_IN_CHAT, // (access for admin | ACCESS ERROR | STATUS ERROR)

    REGISTER, // (access for all)
    LOGIN    // c(access for all)
}