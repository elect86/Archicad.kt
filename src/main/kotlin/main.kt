import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.*

@OptIn(InternalAPI::class)
suspend fun main() {
    val client = HttpClient(CIO)
    val response = client.post("http://localhost:19723") {
        body = """{
    "command": "API.GetProductInfo"
}"""
    }
    println(response.content.readUTF8Line())
    client.close()
}