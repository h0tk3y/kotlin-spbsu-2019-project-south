package Transport

import Transport.ServerRequest
import Transport.ServerResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.HttpClient
import io.ktor.http.cio.websocket.*
import io.ktor.client.features.websocket.*
import io.ktor.http.HttpMethod
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class WebClient (private val host : String, private val port : Int) {
    private val client = HttpClient {
        install(WebSockets)
    }

    private val requests = Channel<ServerRequest>()
    private val responses = Channel<ServerResponse>()

    suspend fun run() {

        val objectMapper = jacksonObjectMapper()
        client.ws(
            method = HttpMethod.Get,
            host = host,
            port = port, path = "/"
        ) {
            while (true) {
                val request = requests.receive()
                outgoing.send(Frame.Text(objectMapper.writeValueAsString(request)))
                val frame = incoming.receive()
                if (frame is Frame.Text) {
                    val response = objectMapper.readValue<ServerResponse>(frame.readText())
                    responses.send(response)
                }
            }
        }
    }

    fun makeRequest(request: ServerRequest) : ServerResponse {
        return runBlocking {
            requests.send(request)
            responses.receive()
        }
    }
}