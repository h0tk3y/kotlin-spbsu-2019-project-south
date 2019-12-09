package transport

enum class RequestType {

    GET_USER,        //get user by id (access for all)
    // userId

    GET_USER_CHATS,  //return all user chats map (ids, name) (access only for target user | ACCESS ERROR)
    // userId

    GET_BLOCKED_USERS, // return all user blocked users map (id, name) (access only for target user | ACCESS ERROR)
    // userId

    GET_CONTACTS, // return all user contacts map (id, name) (access only for target user | ACCESS ERROR)
    // userId

    REMOVE_USER,    // todo

    EDIT_USER,      //edit user data (access only for target user | ACCESS ERROR)
    // userId, userString (new data)

    ADD_CONTACT,    //add contact connection between users (access only for not blocked users |ACCESS ERROR | BL ERROR)
    // userId, contactString (contactId, contactName)

    EDIT_CONTACT,   //edit contact (access only for target user) (ACCESS ERROR)
    // server todo

    REMOVE_CONTACT, // todo
    // server roflan-Ivan

    BLOCK_USER,    // block connection (access only for target user | ACCESS ERROR | STATUS ERROR)
    //  userId, contactString(id)

    UNBLOCK_USER,  // unblock connection (access only for target user | ACCESS ERROR | STATUS ERROR)
    //  userId, contactString(id)




    GET_MESSAGE, // return message by id (access only for chat members | ACCESS ERROR)
    // messageId

    SEND_MESSAGE, // (access only for chat members | ACCESS ERROR)
    // messageString(Message)

    EDIT_MESSAGE, // (access only for author | ACCESS ERROR)
    // messageId, messageString(Message)

    DELETE_MESSAGE, //(access only for author and admins | ACCESS ERROR)
    // messageId





    ADD_CHAT, // create new chat (access for all)
    // chatString(Chat)

    GET_CHAT, // todo

    GET_MESSAGES, // return list of messages (access only for chat members | ACCESS ERROR)
    // chatId

    GET_MEMBERS, // return list of members id (access only for admins | ACCESS ERROR)
    // chatId

    GET_ADMINS, // return list of members id (access only for admins (mb for members - todo) | ACCESS ERROR)
    // chatId

    DELETE_CHAT, // todo

    EDIT_CHAT, // (access only for admins | ACCESS ERROR)
    // chatId, chatString(Chat)

    ADD_MEMBER, // adding(inviting) new member to chat (hz dlya kogo access todo)
    // chatId, memberIdString

    KICK_MEMBER, // (access for admin | ACCESS ERROR | STATUS ERROR)
    // chatId, memberIdString

    LEAVE_CHAT, // (access for members | ACCESS ERROR | STATUS ERROR)
    // server todo

    ADD_ADMIN, // (access for owner | ACCESS ERROR | STATUS ERROR)
    // chatId, adminIdString

    REMOVE_ADMIN, // (access for owner | ACCESS ERROR | STATUS ERROR)
    // chatId, adminIdString




    REGISTER, // (access for all)
    // userString(User)

    LOGIN,    // (access for all)

    IS_ADMIN,

    IS_MEMBER,

    IS_OWNER
}