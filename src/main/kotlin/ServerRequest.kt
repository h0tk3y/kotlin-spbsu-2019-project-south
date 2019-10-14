import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

open class ServerRequest {

    constructor (requestType: TransportType? = null, fieldType: FieldType? = null, id: Long = -1, body : String = "") {
        this.id = id
        this.requestType = requestType
        this.fieldType = fieldType
        this.body = body
    }

    var id : Long = -1
    var requestType : TransportType? = null
    var fieldType : FieldType? = null
    var body : String = ""

    private val objectMapper = jacksonObjectMapper()

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}