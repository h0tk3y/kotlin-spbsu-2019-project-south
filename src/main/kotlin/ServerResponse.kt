import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class ServerResponse {

    constructor (responseType: TransportType? = null, fieldType: FieldType? = null, id: Long = -1, body : String = "") {
        this.id = id
        this.responceType = responseType
        this.fieldType = fieldType
        this.body = body
    }

    private val objectMapper = jacksonObjectMapper()

    var id : Long = -1
    var responceType : TransportType? = null
    var fieldType : FieldType? = null
    var body : String = ""

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}