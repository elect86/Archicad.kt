import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable

@OptIn(InternalAPI::class)
suspend fun main() {
//    val client = HttpClient(CIO)
//    val response = client.post("http://localhost:19723") {
//        body = """{
//    "command": "API.GetProductInfo"
//}"""
//    }
//    println(response.content.readUTF8Line())
//    client.close()


//    val client = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json()
//        }
//    }
//
//    val customer: Customer = client.get("http://localhost:8080/customer/3").body()
//
//    val response: HttpResponse = client.post("http://localhost:8080/customer") {
//        contentType(ContentType.Application.Json)
//        setBody(Customer(3, "Jet", "Brains"))
//    }
}

@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)