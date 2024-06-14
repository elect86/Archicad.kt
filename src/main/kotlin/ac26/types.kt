@file:UseSerializers(UuidSerializer::class)

package ac26

import java.util.*
import Response
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

interface toJson {
    infix fun toJson(jsonBuilder: JsonBuilder)
}

@Serializable
data class AttributeFolder(val attributeType: AttributeType,
                           val path: List<String> = emptyList()) : toJson {
    override fun toJson(jsonBuilder: JsonBuilder) {
        jsonBuilder.apply {
            "attributeType" `=` attributeType
            "path"[path]
        }
    }
}

operator fun AttributeType.plus(path: String) = plus(listOf(path))
operator fun AttributeType.plus(path: List<String>) = AttributeFolder(this, path)

typealias ExecutionResult = Response<Unit>
typealias ExecutionResults = Array<ExecutionResult>

internal inline fun <R> String.listMap(list: String, map: (String) -> R): List<R> {
    val padding = substringBefore("\"$list\": [\n").substringAfterLast('\n')
    return substringAfter("\n$padding\"$list\": [\n$padding    {\n")
        .substringBefore("\n$padding    }\n$padding]") // no trailing `\n`, there might be a comma
        .split("\n$padding    },\n$padding    {\n")
        .map(map)
}

internal val String.executionResults: Response<ExecutionResults>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val results = listMap("executionResults") {
            when {
                "\"success\": true" in it -> Response.success(Unit)
                else -> Response.failure(it)
            }
        }
        return Response.success(results.toTypedArray())
    }

internal val String.commandResponse: Response<Pair<String, String>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val text = substringAfter("addOnCommandResponse\": {\n            \"")
        val key = text.substringBefore('"')
        val value = text.substringAfter("\": ").substringBefore("\n        }\n    }\n}").trim('"')
        return Response.success(key to value)
    }

internal val String.boundingBoxes2D: Response<Array<Response<BoundingBoxes2D>>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val boxes = listMap("boundingBoxes2D") {
            if ("error" in it)
                Response.failure(it)
            else
                Response.success(BoundingBoxes2D(it))
        }
        return Response.success(boxes.toTypedArray())
    }

internal val String.boundingBoxes3D: Response<Array<Response<BoundingBoxes3D>>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val boxes = listMap("boundingBoxes3D") {
            if ("error" in it)
                Response.failure(it)
            else
                Response.success(BoundingBoxes3D(it))
        }
        return Response.success(boxes.toTypedArray())
    }

internal val String.activePenTables: Response<Pair<AttributeId, AttributeId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val modelView = substringAfter("modelViewPenTableId\": {\n            \"attributeId\": {\n                \"guid\": \"")
            .substringBefore('"')
        val layoutBook = substringAfter("layoutBookPenTableId\": {\n            \"attributeId\": {\n                \"guid\": \"")
            .substringBefore('"')
        return Response.success(AttributeId(modelView.uuid) to AttributeId(layoutBook.uuid))
    }

internal val String.allClassificationSystems: Response<List<ClassificationSystem>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val systems = listMap("classificationSystems") {
            val guid = it.substringAfter("classificationSystemId\": {\n                    \"guid\": \"")
                .substringBefore('"')
            val name = it.substringAfter("},\n                \"name\": \"").substringBefore("\",\n")
            val description = it.substringAfter("\",\n                \"description\": \"").substringBefore("\",\n")
            val source = it.substringAfter("\",\n                \"source\": \"").substringBefore("\",\n")
            val version = it.substringAfter("\",\n                \"version\": \"").substringBefore("\",\n")
            val date = it.substringAfter("\",\n                \"date\": \"").substringBeforeLast('"')
            ClassificationSystem(ClassificationSystemId(guid.uuid), name, description, source, version, LocalDate.parse(date))
        }
        return Response.success(systems)
    }

internal val String.allClassificationInSystems: Response<List<ClassificationItem>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("classificationItems", ClassificationItem::invoke)
        return Response.success(items)
    }

internal val String.allElements: Response<List<ElementId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("elements") { ElementId(it.substringAfter("guid\": \"").substringBefore('"').uuid) }
        return Response.success(items)
    }

internal val String.allPropertyGroupIds: Response<List<PropertyGroupId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("propertyGroupIds") { PropertyGroupId(it.substringAfter("guid\": \"").substringBefore('"').uuid) }
        return Response.success(items)
    }

internal val String.allPropertyIds: Response<List<PropertyId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("propertyIds") { PropertyId(it.substringAfter("guid\": \"").substringBefore('"').uuid) }
        return Response.success(items)
    }

internal val String.allPropertyIdsOfElements: Response<List<Response<List<PropertyId>>>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("propertyIdsOfElements") {
            if ("\"error\": {" in it)
                Response.failure(it)
            else
                Response.success(listMap("propertyIds") { id ->
                    PropertyId(id.substringAfter("guid\": \"").substringBefore('"').uuid)
                })
        }
        return Response.success(items)
    }

internal val String.allPropertyNames: Response<List<PropertyUserId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Response.failure(this)
        val items = listMap("properties") {
            if ("type\": \"BuiltIn\"," in it)
                BuildInProperty(it.substringAfter("nonLocalizedName\": \"").substringBefore('"'))
            else {
                val (group, name) = substringAfter("localizedName\": [\n").substringBefore(']')
                    .split(',').map { it.trim(' ', '"', '\n') }
                UserDefinedInProperty(group, name)
            }
        }
        return Response.success(items)
    }

sealed interface PropertyUserId {
    val type: Type

    enum class Type { BuiltIn, UserDefined }
}

data class BuildInProperty(val nonLocalizedName: String) : PropertyUserId {
    override val type = PropertyUserId.Type.BuiltIn
}

data class UserDefinedInProperty(val group: String, val localizedName: String) : PropertyUserId {
    override val type = PropertyUserId.Type.UserDefined
}

// we sacrifice inline class to use these in `vararg` arguments

/*@JvmInline
value*/ class AttributeId(override val guid: UUID) : Id {
    override fun equals(other: Any?) = other is AttributeId && guid == other.guid
    override fun hashCode() = guid.hashCode()
}

/*@JvmInline
value*/ class AttributeFolderId(override val guid: UUID) : Id

@JvmInline
value class ClassificationSystemId(override val guid: UUID) : Id

@JvmInline
value class ClassificationItemId(override val guid: UUID) : Id

@Serializable
/*@JvmInline
value*/ class NavigatorItemId(override val guid: UUID) : Id {

    constructor(guid: String) : this(guid.uuid)

    /** CloneProjectMapItemToViewMap */
    infix fun cloneTo(viewMap: NavigatorItemId): NavigatorItemId =
        CloneProjectMapItemToViewMap(this, viewMap)()

    override fun equals(other: Any?): Boolean = other is NavigatorItemId && guid == other.guid
    override fun hashCode() = guid.hashCode()

    companion object {
        infix fun from(response: String): Response<NavigatorItemId> =
            when (val succeeded = response.substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()) {
                succeeded -> {
                    val guid = response.substringAfter("guid\": \"").substringBefore('"')
                    Response(NavigatorItemId(guid.uuid))
                }
                else -> Response.failure(response)
            }
    }
}

@JvmInline
value class PropertyId(override val guid: UUID) : Id

@JvmInline
value class PropertyGroupId(override val guid: UUID) : Id

/*@JvmInline
value*/ class ElementId(override val guid: UUID) : Id {
    override fun equals(other: Any?) = other is ElementId && guid == other.guid
    override fun hashCode() = guid.hashCode()
}

@JvmInline
value class ComponentId(override val guid: UUID) : Id

@Serializable
data class Layout(val layoutName: String,
                  val masterNavigatorItemId: NavigatorItemId,
                  val parentNavigatorItemId: NavigatorItemId,
                  val parameters: Parameters) {

    fun print(json: JsonBuilder) = json.apply {
        +::layoutName
        +::masterNavigatorItemId
        +::parentNavigatorItemId
        "layoutParameters" {
            +parameters::horizontalSize
            +parameters::verticalSize
            +parameters::leftMargin
            +parameters::topMargin
            +parameters::rightMargin
            +parameters::bottomMargin
            +parameters::customLayoutNumber
            +parameters::customLayoutNumbering
            +parameters::doNotIncludeInNumbering
            +parameters::displayMasterLayoutBelow
            +parameters::layoutPageNumber
            +parameters::actPageIndex
            +parameters::currentRevisionId
            +parameters::currentFinalRevisionId
            +parameters::hasIssuedRevision
            +parameters::hasActualRevision
        }
    }

    class Builder {
        lateinit var name: String
        var masterNavigatorItemId: NavigatorItemId? = null
        var parentNavigatorItemId: NavigatorItemId? = null
        internal lateinit var parameters: Parameters
        fun parameters(init: Parameters.Builder.() -> Unit) {
            parameters = Parameters.Builder().apply(init).build()
        }
    }

    @Serializable
    data class Parameters(val horizontalSize: mm,
                          val verticalSize: mm,
                          val leftMargin: Int,
                          val topMargin: Int,
                          val rightMargin: Int,
                          val bottomMargin: Int,
                          val customLayoutNumber: String,
                          val customLayoutNumbering: Boolean,
                          val doNotIncludeInNumbering: Boolean,
                          val displayMasterLayoutBelow: Boolean,
                          val layoutPageNumber: Int,
                          val actPageIndex: Int,
                          val currentRevisionId: String,
                          val currentFinalRevisionId: String,
                          val hasIssuedRevision: Boolean,
                          val hasActualRevision: Boolean) {
        class Builder {
            var horizontalSize: Int? = null
            var verticalSize: Int? = null
            var leftMargin: Int? = null
            var topMargin: Int? = null
            var rightMargin: Int? = null
            var bottomMargin: Int? = null
            var customLayoutNumber: String? = null
            var customLayoutNumbering: Boolean? = null
            var doNotIncludeInNumbering: Boolean? = null
            var displayMasterLayoutBelow: Boolean? = null
            var layoutPageNumber: Int? = null
            var actPageIndex: Int? = null
            var currentRevisionId: String? = null
            var currentFinalRevisionId: String? = null
            var hasIssuedRevision: Boolean? = null
            var hasActualRevision: Boolean? = null
            fun build() = Parameters(horizontalSize!!, verticalSize!!, leftMargin!!, topMargin!!, rightMargin!!,
                                     bottomMargin!!, customLayoutNumber!!, customLayoutNumbering!!,
                                     doNotIncludeInNumbering!!, displayMasterLayoutBelow!!, layoutPageNumber!!,
                                     actPageIndex!!, currentRevisionId!!, currentFinalRevisionId!!, hasIssuedRevision!!,
                                     hasActualRevision!!)
        }
    }

    companion object {
        fun build(init: Builder.() -> Unit) = Builder().run {
            init()
            Layout(name, masterNavigatorItemId!!, parentNavigatorItemId!!, parameters)
        }
    }
}

data class LayoutSubset(val parentNavigatorItemId: NavigatorItemId,
                        val parameters: Parameters) {

    fun print(json: JsonBuilder) = json.apply {
        +::parentNavigatorItemId
        "subsetParameters" {
            +parameters::name
            +parameters::includeToIDSequence
            +parameters::customNumbering
            +parameters::continueNumbering
            +parameters::useUpperPrefix
            +parameters::addOwnPrefix
            +parameters::customNumber
            +parameters::autoNumber
            +parameters::numberingStyle
            +parameters::startAt
            +parameters::ownPrefix
        }
    }

    class Builder {
        var parentNavigatorItemId: NavigatorItemId? = null
        internal lateinit var parameters: Parameters
        fun parameters(init: Parameters.Builder.() -> Unit) {
            parameters = Parameters.Builder().apply(init).build()
        }
    }

    data class Parameters(val name: String,
                          val includeToIDSequence: Boolean,
                          val customNumbering: Boolean,
                          val continueNumbering: Boolean,
                          val useUpperPrefix: Boolean,
                          val addOwnPrefix: Boolean,
                          val customNumber: String,
                          val autoNumber: String,
                          val numberingStyle: NumberingStyle,
                          val startAt: Int,
                          val ownPrefix: String) {

        class Builder {
            lateinit var name: String
            var includeToIDSequence: Boolean? = null
            var customNumbering: Boolean? = null
            var continueNumbering: Boolean? = null
            var useUpperPrefix: Boolean? = null
            var addOwnPrefix: Boolean? = null
            lateinit var customNumber: String
            lateinit var autoNumber: String
            lateinit var numberingStyle: NumberingStyle
            var startAt: Int? = null
            lateinit var ownPrefix: String
            fun build() = Parameters(name, includeToIDSequence!!, customNumbering!!, continueNumbering!!,
                                     useUpperPrefix!!, addOwnPrefix!!, customNumber, autoNumber, numberingStyle,
                                     startAt!!, ownPrefix)
        }
    }

    companion object {
        fun build(init: Builder.() -> Unit) = Builder().run {
            init()
            LayoutSubset(parentNavigatorItemId!!, parameters)
        }
    }
}

enum class NumberingStyle { Undefined, abc, ABC, `1`, `01`, `001`, `0001`, noID }

data class ViewMapFolder(val folderName: String,
                         val parentNavigatorItemId: NavigatorItemId?,
                         val previousNavigatorItemId: NavigatorItemId?) {

    fun print(json: JsonBuilder) = json.apply {
        "folderParameters" {
            "name" `=` folderName
        }
        +::parentNavigatorItemId
        +::previousNavigatorItemId
    }

    class Builder {
        lateinit var folderName: String
        var parentNavigatorItemId: NavigatorItemId? = null
        var previousNavigatorItemId: NavigatorItemId? = null
    }

    companion object {
        fun build(init: Builder.() -> Unit) = Builder().run {
            init()
            ViewMapFolder(folderName, parentNavigatorItemId, previousNavigatorItemId)
        }
    }
}

class ExecuteAddOnCommand(val commandNamespace: String,
                          val commandName: String,
                          val parameters: Array<out Pair<String, String>>) {

    fun print(json: JsonBuilder) = json.apply {
        "addOnCommandId" {
            +::commandNamespace
            +::commandName
        }
        if (parameters.isNotEmpty())
            "addOnCommandParameters" {
                for ((k, v) in parameters)
                    +"\"$k\": \"$v\""
            }
    }

    class Builder {
        lateinit var commandNamespace: String
        lateinit var commandName: String
        var parameters: Array<out Pair<String, String>> = emptyArray()
        fun parameters(vararg args: Pair<String, String>) {
            parameters = args
        }
    }

    companion object {
        fun build(init: Builder.() -> Unit) = Builder().run {
            init()
            ExecuteAddOnCommand(commandNamespace, commandName, parameters)
        }
    }
}

class Get2DBoundingBoxes(val elements: Array<out ElementId>) {
    fun print(json: JsonBuilder) = json.apply { "elements"["elementId", elements] }
}

data class BoundingBoxes2D(val xMin: Double, val yMin: Double,
                           val xMax: Double, val yMax: Double) {

    constructor(string: String) : this(string.substringAfter("xMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("xMax\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMax\": ").substringBefore('\n').toDouble())
}

class Get3DBoundingBoxes(val elements: Array<out ElementId>) {
    fun print(json: JsonBuilder) = json.apply { "elements"["elementId", elements] }
}

data class BoundingBoxes3D(val xMin: Double, val yMin: Double, val zMin: Double,
                           val xMax: Double, val yMax: Double, val zMax: Double) {

    constructor(string: String) : this(string.substringAfter("xMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("zMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("xMax\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMax\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("zMax\": ").substringBefore('\n').toDouble())
}

data class ClassificationSystem(val classificationSystemId: ClassificationSystemId,
                                val name: String,
                                val description: String,
                                val source: String,
                                val version: String,
                                val date: LocalDate)

data class ClassificationItem(val classificationItemId: ClassificationItemId,
                              val id: String,
                              val name: String,
                              val description: String,
                              val children: List<ClassificationItem>) {

    companion object {
        operator fun invoke(text: String): ClassificationItem {
            val guid = text.substringAfter("guid\": \"").substringBefore('"')
            val id = text.substringAfter("\"id\": \"").substringBefore('"')
            val name = text.substringAfter("name\": \"").substringBefore('"')
            val description = text.substringAfter("description\": \"").substringBefore('"')
            val children = when {
                "\"children\": [\n" in text -> text.listMap("children", ClassificationItem::invoke)
                else -> emptyList()
            }
            return ClassificationItem(ClassificationItemId(guid.uuid), id, name, description, children)
        }
    }
}