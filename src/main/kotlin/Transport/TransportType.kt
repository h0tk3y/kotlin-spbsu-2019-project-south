enum class TransportType {

    GET_USER,        //get user by id
    GET_USER_CHATS,  //return all user chats map (ids, name)
    REMOVE_USER,    // todo
    EDIT_USER,      //edit user data
    ADD_CONTACT,    //add contact connection between users
    EDIT_CONTACT,   //edit contact
    REMOVE_CONTACT, // todo
    BLOCK_USER,    // block connection
    UNBLOCK_USER,  // unblock connection
    GET_BLOCKED_USERS, // return all user blocked users map (id, name)
    GET_CONTACTS, // return all user contacts map (id, name)

    SEND_MESSAGE,
    EDIT_MESSAGE,
    DELETE_MESSAGE,
    GET_MESSAGE, // return message by id


    ADD_CHAT, // create new chat
    GET_CHAT, // get chat by id
    GET_MESSAGES, // return list of messages
    GET_MEMBERS, // return list of members id
    GET_ADMINS, // return list of members id
    DELETE_CHAT, // todo
    EDIT_CHAT,
    ADD_MEMBER, // adding(inviting) new member to chat
    JOIN_CHAT, // join to chat
    KICK_MEMBER,
    LEAVE_CHAT,
    ADD_ADMIN,
    REMOVE_ADMIN,

    REGISTER,
    LOGIN
}