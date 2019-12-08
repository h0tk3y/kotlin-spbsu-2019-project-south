package DataClasses

import User

data class LoginData(val login: String, val password: String) {
    var id: Long = -1;
    var jwt: String = "";
    var user = User(-1)
}