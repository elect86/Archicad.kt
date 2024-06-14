import ac26.Archicad26
import ac26.NavigatorItemId
import ac26.uuid
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class CreateViewMapFolder {

    val command = """|{
                     |    "command": "API.CreateViewMapFolder",
                     |    "parameters": {
                     |        "folderParameters": {
                     |            "name": "Test Folder"
                     |        },
                     |        "parentNavigatorItemId": {
                     |            "guid": "df27178e-67e0-4d70-83aa-d0e8d4530440"
                     |        },
                     |        "previousNavigatorItemId": {
                     |            "guid": "14807f55-fd6c-41ea-a6c6-160e57eed613"
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "createdFolderNavigatorItemId": {
                      |            "guid": "edb6197b-0754-4741-8ba6-712865fd0f76"
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val createdFolderNavigatorItemId = Archicad26.createViewMapFolder {
            folderName = "Test Folder"
            parentNavigatorItemId = NavigatorItemId("df27178e-67e0-4d70-83aa-d0e8d4530440".uuid)
            previousNavigatorItemId = NavigatorItemId("14807f55-fd6c-41ea-a6c6-160e57eed613".uuid)
        }
        assert(createdFolderNavigatorItemId.isSuccess)
        assert(createdFolderNavigatorItemId() == NavigatorItemId("edb6197b-0754-4741-8ba6-712865fd0f76".uuid))
    }
}