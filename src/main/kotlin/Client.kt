import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.`KotlinPackage-Extensions-6da2b0d6`.jacksonObjectMapper


class Client() {
    var loggedUserId: Long = -1

    private val objectMapper = jacksonObjectMapper()

    class userData(val userId: Long)
    class messageData(val messageId: Long)
    class chatData(val chatId: Long)
}