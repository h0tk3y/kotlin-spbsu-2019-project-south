package Transport

import TransportType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class ServerResponse(
    var status: ResponseStatus = ResponseStatus.SUCCESSFUL,
    var id: Long = -1,
    var body: String = ""
) {

    private val objectMapper = jacksonObjectMapper()

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}