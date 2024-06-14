import ac26.Archicad26
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

fun mock(command: String, response: String) {
    Archicad26.client = HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        engine {
            addHandler {
//                                println("`$command`")
                // drop last `\n`
                val body = it.body.toByteArray().decodeToString()
                //                println("`$body`")
//                check(body == command)
                check(it.url.encodedPath.isEmpty())
                respond(response, headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }
        }
    }
}

/*
differences a26/27
- attributeFolders

 */
