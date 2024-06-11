package ac26

import Result
import io.ktor.client.*
import io.ktor.client.engine.cio.*

object Archicad {
    var client = HttpClient(CIO)

    fun createAttributeFolders(vararg attributeFolders: AttributeFolder): Result<ExecutionResults> =
        command("CreateAttributeFolders") { "attributeFolders"[attributeFolders] }.executionResults

    fun createLayout(init: Layout.Builder.() -> Unit): Result<NavigatorItemId> =
        NavigatorItemId from command("CreateLayout") { Layout.build(init).print(this) }

    fun createLayoutSubset(init: LayoutSubset.Builder.() -> Unit): Result<NavigatorItemId> =
        NavigatorItemId from command("CreateLayoutSubset") { LayoutSubset.build(init).print(this) }

    fun createViewMapFolder(init: ViewMapFolder.Builder.() -> Unit): Result<NavigatorItemId> =
        NavigatorItemId from command("CreateViewMapFolder") { ViewMapFolder.build(init).print(this) }

    fun deleteAttributeFolders(vararg attributeFolderIds: AttributeFolderId): Result<ExecutionResults> =
        command("DeleteAttributeFolders") { "attributeFolderIds"[attributeFolderIds] }.executionResults

    fun deleteAttributes(vararg attributeIds: AttributeId): Result<ExecutionResults> =
        command("DeleteAttributes") { "attributeIds"[attributeIds] }.executionResults

    fun deleteNavigatorItems(vararg navigatorItemIds: NavigatorItemId): Result<ExecutionResults> =
        command("DeleteNavigatorItems") { "navigatorItemIds"[navigatorItemIds] }.executionResults

    fun executeAddOnCommand(init: ExecuteAddOnCommand.Builder.() -> Unit): Result<Pair<String, String>> =
        command("ExecuteAddOnCommand") { ExecuteAddOnCommand.build(init).print(this) }.commandResponse

    fun get2dBoundingBoxes(vararg elementIds: ElementId): Result<Array<Result<BoundingBoxes2D>>> =
        command("Get2DBoundingBoxes") { Get2DBoundingBoxes(elementIds).print(this) }.boundingBoxes2D

    fun get3dBoundingBoxes(vararg elementIds: ElementId): Result<Array<Result<BoundingBoxes3D>>> =
        command("Get3DBoundingBoxes") { Get3DBoundingBoxes(elementIds).print(this) }.boundingBoxes3D

    val activePenTables: Result<Pair<AttributeId, AttributeId>>
        get() = command("GetActivePenTables").activePenTables

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