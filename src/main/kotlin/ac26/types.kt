package ac26

import java.util.*
import Result
import java.lang.StringBuilder
import kotlin.reflect.KProperty0

interface toJson {
    infix fun toJson(jsonBuilder: JsonBuilder)
}

data class AttributeFolder(val attributeType: AttributeType, val path: List<String> = emptyList()) : toJson {
    override fun toJson(jsonBuilder: JsonBuilder) {
        jsonBuilder.apply {
            "attributeType" `=` attributeType
            "path"[path]
        }
    }
}

operator fun AttributeType.plus(path: String) = plus(listOf(path))
operator fun AttributeType.plus(path: List<String>) = AttributeFolder(this, path)

typealias ExecutionResult = Result<Unit>
typealias ExecutionResults = Array<ExecutionResult>

internal val String.executionResults: Result<ExecutionResults>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Result.failure(this)
        val results = substringAfter("\"executionResults\": [\n").substringBefore("\n        ]\n    }\n}")
                .split("\n            },\n            {\n")
                .map {
                    when {
                        "\"success\": true" in it -> Result.success(Unit)
                        else -> Result.failure(it)
                    }
                }
        return Result.success(results.toTypedArray())
    }

internal val String.commandResponse: Result<Pair<String, String>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Result.failure(this)
        val text = substringAfter("addOnCommandResponse\": {\n            \"")
        val key = text.substringBefore('"')
        val value = text.substringAfter("\": ").substringBefore("\n        }\n    }\n}").trim('"')
        return Result.success(key to value)
    }

internal val String.boundingBoxes2D: Result<Array<Result<BoundingBoxes2D>>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Result.failure(this)
        val boxes = substringAfter("boundingBoxes2D\": [\n            {\n")
                .substringBefore("\n            }\n        ]\n    }\n}")
                .split("\n            },\n            {\n")
                .map {
                    if ("error" in it)
                        Result.failure(it)
                    else
                        Result.success(BoundingBoxes2D(it))
                }
        return Result.success(boxes.toTypedArray())
    }

internal val String.boundingBoxes3D: Result<Array<Result<BoundingBoxes3D>>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Result.failure(this)
        val boxes = substringAfter("boundingBoxes3D\": [\n            {\n")
                .substringBefore("\n            }\n        ]\n    }\n}")
                .split("\n            },\n            {\n")
                .map {
                    if ("error" in it)
                        Result.failure(it)
                    else
                        Result.success(BoundingBoxes3D(it))
                }
        return Result.success(boxes.toTypedArray())
    }

internal val String.activePenTables: Result<Pair<AttributeId, AttributeId>>
    get() {
        val succeeded = substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()
        if (!succeeded)
            return Result.failure(this)
        val modelView = substringAfter("modelViewPenTableId\": {\n            \"attributeId\": {\n                \"guid\": \"")
                .substringBefore('"')
        val layoutBook = substringAfter("layoutBookPenTableId\": {\n            \"attributeId\": {\n                \"guid\": \"")
                .substringBefore('"')
        return Result.success(AttributeId(modelView.uuid) to AttributeId(layoutBook.uuid))
    }

// we sacrifice inline class to use these in `vararg` arguments

/*@JvmInline
value*/ class AttributeId(override val guid: UUID) : Id {
    override fun equals(other: Any?) = other is AttributeId && guid == other.guid
}

/*@JvmInline
value*/ class AttributeFolderId(override val guid: UUID) : Id

@JvmInline
value class ClassificationSystemId(override val guid: UUID) : Id

@JvmInline
value class ClassificationItemId(override val guid: UUID) : Id

/*@JvmInline
value*/ class NavigatorItemId(override val guid: UUID) : Id {

    /** CloneProjectMapItemToViewMap */
    infix fun cloneTo(viewMap: NavigatorItemId): Result<NavigatorItemId> =
        from(command("CloneProjectMapItemToViewMap") {
            "projectMapNavigatorItemId" `=` this@NavigatorItemId
            "parentNavigatorItemId" `=` viewMap
        })

    companion object {
        infix fun from(response: String): Result<NavigatorItemId> =
            when (val succeeded = response.substringAfter("succeeded\": ").substringBefore(',').toBooleanStrict()) {
                succeeded -> {
                    val guid = response.substringAfter("guid\": \"").substringBefore('"')
                    Result(NavigatorItemId(guid.uuid))
                }
                else -> Result.failure(response)
            }
    }
}

@JvmInline
value class PropertyId(override val guid: UUID) : Id

@JvmInline
value class PropertyGroupId(override val guid: UUID) : Id

/*@JvmInline
value*/ class ElementId(override val guid: UUID) : Id

@JvmInline
value class ComponentId(override val guid: UUID) : Id

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
                         val parentNavigatorItemId: NavigatorItemId,
                         val previousNavigatorItemId: NavigatorItemId) {

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
            ViewMapFolder(folderName, parentNavigatorItemId!!, previousNavigatorItemId!!)
        }
    }
}

class DeleteAttributeFolder(vararg attributeFolderIds: Any) {
    @Suppress("UNCHECKED_CAST")
    val attributeFolderIds = attributeFolderIds as Array<NavigatorItemId>
}

data class ExecuteAddOnCommand(val commandNamespace: String,
                               val commandName: String,
                               val parameters: List<Pair<String, String>>) {

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
        lateinit var parameters: List<Pair<String, String>>
        fun parameters(vararg args: Pair<String, String>) {
            parameters = args.toList()
        }
    }

    companion object {
        fun build(init: Builder.() -> Unit) = Builder().run {
            init()
            ExecuteAddOnCommand(commandNamespace, commandName, parameters)
        }
    }
}

data class Get2DBoundingBoxes(val elements: Array<out ElementId>) {
    fun print(json: JsonBuilder) = json.apply { "elements"["elementId", elements] }
}

data class BoundingBoxes2D(val xMin: Double, val yMin: Double,
                           val xMax: Double, val yMax: Double) {

    constructor(string: String) : this(string.substringAfter("xMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMin\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("xMax\": ").substringBefore(',').toDouble(),
                                       string.substringAfter("yMax\": ").substringBefore('\n').toDouble())
}

data class Get3DBoundingBoxes(val elements: Array<out ElementId>) {
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