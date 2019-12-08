import transport.RequestType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

open class ServerRequest(
    var requestType: RequestType? = null,
    var id: Long = -1,
    var body: String = "",
    var jwt: String = ""
) {

    private val objectMapper = jacksonObjectMapper()

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}