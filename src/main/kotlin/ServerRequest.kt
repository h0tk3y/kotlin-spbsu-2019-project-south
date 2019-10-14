import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

open class ServerRequest {

    constructor (reqType: ReqType? = null, fieldType: FieldType? = null, id: Long = -1, body : String = "") {
        this.id = id
        this.reqType = reqType
        this.fieldType = fieldType
        this.body = body
    }

    var id : Long = -1
    var reqType : ReqType? = null
    var fieldType : FieldType? = null
    var body : String = ""

    private val objectMapper = jacksonObjectMapper()

    override fun toString(): String {
        return objectMapper.writeValueAsString(this)
    }
}