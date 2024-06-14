package ac26

import Response
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object Archicad26 {

    var client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
//                serializersModule = SerializersModule {
//                    polymorphic(Command::class) {
//                        subclass(CloneProjectMapItemToViewMap::class)
//                    }
//                }
            })
        }
    }

    // different
    fun createAttributeFolders(vararg attributeFolders: AttributeFolder): ExecutionResults =
        CreateAttributeFolders(attributeFolders)()
//        command("CreateAttributeFolders") { "attributeFolders"[attributeFolders] }.executionResults

    fun createLayout(init: Layout.Builder.() -> Unit): Response<NavigatorItemId> =
        NavigatorItemId from command("CreateLayout") { Layout.build(init).print(this) }

    fun createLayoutSubset(init: LayoutSubset.Builder.() -> Unit): Response<NavigatorItemId> =
        NavigatorItemId from command("CreateLayoutSubset") { LayoutSubset.build(init).print(this) }

    fun createViewMapFolder(init: ViewMapFolder.Builder.() -> Unit): Response<NavigatorItemId> =
        NavigatorItemId from command("CreateViewMapFolder") { ViewMapFolder.build(init).print(this) }

    // different
    fun deleteAttributeFolders(vararg attributeFolderIds: AttributeFolderId): Response<ExecutionResults> =
        command("DeleteAttributeFolders") { "attributeFolderIds"[attributeFolderIds] }.executionResults

    fun deleteAttributes(vararg attributeIds: AttributeId): Response<ExecutionResults> =
        command("DeleteAttributes") { "attributeIds"[attributeIds] }.executionResults

    fun deleteNavigatorItems(vararg navigatorItemIds: NavigatorItemId): Response<ExecutionResults> =
        command("DeleteNavigatorItems") { "navigatorItemIds"[navigatorItemIds] }.executionResults

    fun executeAddOnCommand(init: ExecuteAddOnCommand.Builder.() -> Unit): Response<Pair<String, String>> =
        command("ExecuteAddOnCommand") { ExecuteAddOnCommand.build(init).print(this) }.commandResponse

    fun get2dBoundingBoxes(vararg elementIds: ElementId): Response<Array<Response<BoundingBoxes2D>>> =
        command("Get2DBoundingBoxes") { Get2DBoundingBoxes(elementIds).print(this) }.boundingBoxes2D

    fun get3dBoundingBoxes(vararg elementIds: ElementId): Response<Array<Response<BoundingBoxes3D>>> =
        command("Get3DBoundingBoxes") { Get3DBoundingBoxes(elementIds).print(this) }.boundingBoxes3D

    val activePenTables: Response<Pair<AttributeId, AttributeId>>
        get() = command("GetActivePenTables").activePenTables

    val allClassificationSystem: Response<List<ClassificationSystem>>
        get() = command("GetAllClassificationSystems").allClassificationSystems

    infix fun getAllClassificationInSystem(classificationSystemId: ClassificationSystemId): Response<List<ClassificationItem>> =
        command("GetAllClassificationInSystems") { "classificationSystemId" `=` classificationSystemId }.allClassificationInSystems

    val allElements: Response<List<ElementId>>
        get() = command("GetAllElements").allElements

    val allPropertyGroupIds: Response<List<PropertyGroupId>>
        get() = command("GetAllPropertyGroupIds") {}.allPropertyGroupIds

    infix fun getAllPropertyGroupIds(propertyType: String): Response<List<PropertyGroupId>> =
        command("GetAllPropertyGroupIds") { "propertyType" `=` propertyType }.allPropertyGroupIds

    val allPropertyIds: Response<List<PropertyId>>
        get() = command("GetAllPropertyIds") {}.allPropertyIds

    infix fun getAllPropertyIds(propertyType: String): Response<List<PropertyId>> =
        command("GetAllPropertyIds") { "propertyType" `=` propertyType }.allPropertyIds

    fun getAllPropertyIdsOfElements(vararg elements: ElementId, propertyType: String? = null): Response<List<Response<List<PropertyId>>>> =
        command("GetAllPropertyIdsOfElements") {
            "elements"["elementId", elements]
            propertyType?.let { "propertyType" `=` it }
        }.allPropertyIdsOfElements

    val allPropertyNames: Response<List<PropertyUserId>>
        get() = command("GetAllPropertyNames").allPropertyNames


    //    val productInfo
    //        get() = "API.GetProductInfo"()

    //    operator fun String.invoke(vararg args: Pair<String, String>) {
    //        runBlocking {
    //            client.post("http://localhost:19723") {
    //                body = buildString {
    //                    appendLine("{\n    \"command\": \"$this\"")
    //                    if (args.isNotEmpty()) {
    //                        appendLine("    \"parameters\": {")
    //                        for ((name, value) in args)
    //                            appendLine("        \"$name\": \"$value\"")
    //                        appendLine("    }")
    //                    }
    //                    append('}')
    //                }
    //            }
    //        }
    //    }
}

//operator fun String.invoke(vararg args: Pair<String, Id>) {
//    runBlocking {
//        Archicad.client.post("http://localhost:19723") {
//            body = buildString {
//                appendLine("{\n    \"command\": \"API.$this\"")
//                if (args.isNotEmpty()) {
//                    appendLine("    \"parameters\": {")
//                    for ((name, value) in args)
//                        appendLine("        \"$name\": \"$value\"")
//                    appendLine("    }")
//                }
//                append('}')
//            }
//        }
//    }
//}