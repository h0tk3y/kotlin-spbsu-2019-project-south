import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

open class ServerRequest {

    constructor (requestType: RequestType? = null, fieldType: FieldType? = null, id: Long = -1, body : String = "") {
        this.id = id
        this.requestType = requestType
        this.fieldType = fieldType
        this.body = body
    }

    constructor(requestString: String) {
        TODO()
    }

    var id : Long = -1
    var requestType : RequestType? = null
    var fieldType : FieldType? = null
    var body : String = ""

    private val objectMapper = jacksonObjectMapper()

    override fun toString(): String {
        TODO()
    }

    private lateinit var server : Server

    fun makeRequest() = server.answerRequest(this.toString())
}