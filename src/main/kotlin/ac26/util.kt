@file:OptIn(InternalAPI::class)

package ac26

import createdNavigatorItemId
import error
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import result
import succeeded
import java.util.*
import kotlin.reflect.KProperty0


sealed interface Id {
    val guid: UUID
}

enum class AttributeType { BuildingMaterial, Composite, Fill, Layer, LayerCombination, Line, PenTable, Profile, Surface, ZoneCategory }

val String.uuid: UUID
    get() = UUID.fromString(this)
val String.succeeded: Boolean
    get() = startsWith("{\n    \"succeeded\": true,")

suspend fun ByteReadChannel.readText(): String = buildString {
    while (!isClosedForRead)
        appendLine(readUTF8Line())
    return toString()
}



fun command(command: String, block: (JsonBuilder.() -> Unit)? = null): String = runBlocking {
    Archicad26.client.post("http://localhost:19723") {
        body = buildJson {
            if (block == null)
                +"\"command\": \"API.$command\""
            else {
                +"\"command\": \"API.$command\","
                "parameters" {
                    block()
                }
            }
        }
    }.content.readText()
}

inline fun buildJson(block: JsonBuilder.() -> Unit): String = JsonBuilder().run {
    graphs {
        block()
    }
    stringBuilder.toString()
}

class JsonBuilder {

    val stringBuilder = StringBuilder()
    var indents = 0
    val indent
        get() = "    ".repeat(indents)

    inline fun <R> indent(block: () -> R): R {
        indents++
        return block().also { indents-- }
    }

    val lastPositions = Stack<Int>()
    val firstInScope = Stack<Boolean>().apply { push(false) }

    inline fun graphs(block: () -> Unit) {
        maybeComma()
        +'{'
        firstInScope += true
        val size = lastPositions.size
        indent { block() }
        +'}'
        if (size != lastPositions.size) lastPositions.pop()
        firstInScope.pop()
        savePosition()
    }

    operator fun Char.unaryPlus() = +toString()

    operator fun String.unaryPlus() {
        appendLine(this)
    }

    operator fun String.invoke(block: JsonBuilder.() -> Unit) {
        maybeComma()
        +"\"$this\": {"
        firstInScope.pop()
        firstInScope += false
        //        firstInScope += true
        //        val size = lastPositions.size
        indent { block() }
        +'}'
        //        if (size != lastPositions.size) lastPositions.pop()
        //        firstInScope.pop()
        savePosition()
    }

    @JvmName("get0")
    operator fun String.get(name: String, ids: Array<out Id>) {
        maybeComma()
        +"\"$this\": ["
        indent {
            firstInScope += true
            for (id in ids) {
                graphs {
                    name `=` id
                }
                firstInScope.pop()
                firstInScope += false
                savePosition()
            }
            lastPositions.pop()
        }
        +']'
        firstInScope.pop()
        firstInScope += false
        savePosition()
    }

    @JvmName("get0")
    operator fun String.get(ids: Array<out Id>) = get(this.dropLast(1), ids)

    operator fun String.get(elements: Array<out toJson>) {
        maybeComma()
        +"\"$this\": ["
        indent {
            firstInScope += true
            for (element in elements) {
                graphs {
                    element toJson this@JsonBuilder
                }
                firstInScope.pop()
                firstInScope += false
                savePosition()
            }
            lastPositions.pop()
        }
        +']'
        firstInScope.pop()
        firstInScope += false
        savePosition()
    }

    operator fun String.get(elements: List<String>) {
        maybeComma()
        +"\"$this\": ["
        indent {
            firstInScope += true
            for (element in elements) {
                maybeComma()
                +"\"$element\""
                firstInScope.pop()
                firstInScope += false
                savePosition()
            }
            lastPositions.pop()
        }
        +']'
        firstInScope.pop()
        firstInScope += false
        savePosition()
    }

    infix fun String.`=`(any: Any) = `=`(any.toString(), false)
    infix fun String.`=`(s: String) = `=`(s, true)

    infix fun <E : Enum<E>> String.`=`(enum: E) = `=`(enum.name)

    fun String.`=`(string: String, doubleQuotes: Boolean = true) {
        maybeComma()
        firstInScope.pop()
        firstInScope += false
        if (doubleQuotes) +"\"$this\": \"$string\""
        else +"\"$this\": $string"
        savePosition()
    }

    infix fun String.`=`(id: Id) {
        maybeComma()
        firstInScope.pop()
        firstInScope += false
        this { +"\"guid\": \"${id.guid}\"" }
        savePosition()
    }

    fun savePosition() {
        if (lastPositions.isNotEmpty())
            lastPositions.pop()
        lastPositions += stringBuilder.lastIndex
    }

    fun maybeComma() {
        if (!firstInScope.peek() && lastPositions.isNotEmpty())
            stringBuilder.insert(lastPositions.pop(), ',')
    }

    @JvmName("upA")
    operator fun KProperty0<Any>.unaryPlus() {
        maybeComma()
        firstInScope.pop()
        firstInScope += false
        val maybeDoubleQuote = if (get() is String || get() is Enum<*>) "\"" else ""
        +"\"$name\": $maybeDoubleQuote${get()}$maybeDoubleQuote"
        savePosition()
    }

    @JvmName("upN?")
    operator fun KProperty0<NavigatorItemId?>.unaryPlus() {
        val guid = get()?.guid ?: return
        maybeComma()
        firstInScope.pop()
        firstInScope += false
        name { +"\"guid\": \"$guid\"" }
        savePosition()
    }

    @JvmName("upN")
    operator fun KProperty0<NavigatorItemId>.unaryPlus() {
        val guid = get().guid
        maybeComma()
        firstInScope.pop()
        firstInScope += false
        name { +"\"guid\": \"$guid\"" }
        savePosition()
    }

    fun isNotEmpty() = stringBuilder.toString().isNotEmpty()
    fun append(c: Char) = stringBuilder.append(indent + c)
    fun append(s: String) = stringBuilder.append(indent + s)
    fun appendLine(s: String) = stringBuilder.appendLine(indent + s)
}

typealias mm = Int

@Serializable
class ArchicadError(val code: Int, override val message: String): Exception(message){
    override fun toString() = "$message ($code)"
}