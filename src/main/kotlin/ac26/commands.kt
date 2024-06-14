@file:UseSerializers(UuidSerializer::class)

package ac26

import Response
import createdNavigatorItemId
import error
import executionResults
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import result
import succeeded
import java.util.*

@Serializable
sealed interface Command {

    operator fun<R> invoke(): R {
        val json = request()
        if (!json.succeeded)
            throw json.error.decode<ArchicadError>()
        return json.decode()
    }

    fun <R> JsonElement.decode(): R
}

suspend fun HttpResponse.json(): JsonElement = Json.parseToJsonElement(bodyAsText())
inline fun <reified T> JsonElement.to(): T = Json.decodeFromJsonElement(this)

internal inline fun <reified C : Command> C.request(): JsonElement = runBlocking {
    Archicad26.client.post("http://localhost:19723") {
        contentType(ContentType.Application.Json)
        setBody(this@request)
    }.json()
}


//    if (!json.succeeded)
//        throw json.error!!.decode<ArchicadError>()
//    when (C::class) {
//        CloneProjectMapItemToViewMap::class -> json.result!!.createdNavigatorItemId!!.decode()
//        else -> response.body()
//    }
//}

@Serializable
class CloneProjectMapItemToViewMap(val command: String,
                                   val parameters: Parameters) : Command {
    constructor(projectMapNavigatorItemId: NavigatorItemId,
                parentNavigatorItemId: NavigatorItemId) : this("API.CloneProjectMapItemToViewMap",
                                                               Parameters(projectMapNavigatorItemId, parentNavigatorItemId))
    @Serializable
    class Parameters(val projectMapNavigatorItemId: NavigatorItemId,
                     val parentNavigatorItemId: NavigatorItemId)

    override fun <R> JsonElement.decode(): R = result.createdNavigatorItemId.to<NavigatorItemId>() as R
}

object UuidSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString().uppercase())
    override fun deserialize(decoder: Decoder): UUID = decoder.decodeString().uuid
}

@Serializable
class CreateAttributeFolders(val command: String,
                             val parameters: Parameters) : Command {
    constructor(attributeFolders: Array<out AttributeFolder>) : this("API.CreateAttributeFolders",
                                                                     Parameters(attributeFolders))

    @Serializable
    class Parameters(val attributeFolders: Array<out AttributeFolder>)

    override fun <R> JsonElement.decode(): R = result.executionResults.to<ExecutionResults>() as R
        //ExecutionResults(result.executionResults.size) { result.executionResults[it].to<Response>() }
}

@Serializable
class CreateLayout private constructor(val command: String,
                                       val parameters: Parameters) : Command {
    @Serializable
    class Parameters private constructor(val attributeFolders: List<AttributeFolder>)

    override fun <R> JsonElement.decode(): R {
        TODO("Not yet implemented")
    }
}

