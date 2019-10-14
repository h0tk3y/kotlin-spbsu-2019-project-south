import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ServerResponse {

    constructor (reqType: ReqType? = null, fieldType: FieldType? = null, id: Long = -1, body : String = "") {
        this.id = id
        this.reqType = reqType
        this.fieldType = fieldType
        this.body = body
    }

    private val objectMapper = jacksonObjectMapper()

    var id : Long = -1
    var reqType : ReqType? = null
    var fieldType : FieldType? = null
    var body : String = ""

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}