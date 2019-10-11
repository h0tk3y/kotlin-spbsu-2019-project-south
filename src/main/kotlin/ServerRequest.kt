import com.fasterxml.jackson.module.kotlin.`KotlinPackage-Extensions-6da2b0d6`

open class ServerRequest {

    constructor (reqType: ReqType? = null, fieldType: FieldType? = null, id: Long = -1, body : String? = null) {
        this.id = id
        this.reqType = reqType
        this.fieldType = fieldType
        this.body = body
    }

    constructor(requestString: String) {
        TODO()
    }

    var id : Long = -1
    var reqType : ReqType? = null
    var fieldType : FieldType? = null
    var body : String? = null

    private val objectMapper = `KotlinPackage-Extensions-6da2b0d6`.jacksonObjectMapper()

    override fun toString(): String {
        TODO()
    }

    private lateinit var server : Server

    fun makeRequest() = server.answerRequest(this.toString())
}