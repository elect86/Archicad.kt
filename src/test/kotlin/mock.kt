import ac26.Archicad
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*

fun mock(command: String, response: String) {
    Archicad.client = HttpClient(MockEngine) {
        engine {
            addHandler {
                println("`$command`")
                // drop last `\n`
                val body = it.body.toByteArray().decodeToString().dropLast(1)
                println("`$body`")
                check(body == command)
                check(it.url.encodedPath.isEmpty())
                respond(response, HttpStatusCode.OK)
            }
        }
    }
}