import ac26.Archicad
import ac26.AttributeFolderId
import ac26.uuid
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class DeleteAttributeFolders {

    val command = """|{
                     |    "command": "API.DeleteAttributeFolders",
                     |    "parameters": {
                     |        "attributeFolderIds": [
                     |            {
                     |                "attributeFolderId": {
                     |                    "guid": "131a53c0-3266-4c84-b77f-85aa0df6e9ba"
                     |                }
                     |            },
                     |            {
                     |                "attributeFolderId": {
                     |                    "guid": "b470082a-9187-48e1-acfd-00000bad0000"
                     |                }
                     |            },
                     |            {
                     |                "attributeFolderId": {
                     |                    "guid": "b470082a-9187-48e1-acfd-80598f750d75"
                     |                }
                     |            }
                     |        ]
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "executionResults": [
                      |            {
                      |                "success": true
                      |            },
                      |            {
                      |                "success": false,
                      |                "error": {
                      |                    "code": 6108,
                      |                    "message": "Attribute folder not found: \"b470082a-9187-48e1-acfd-00000bad0000\"."
                      |                }
                      |            },
                      |            {
                      |                "success": true
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val executionResults = Archicad.deleteAttributeFolders(AttributeFolderId("131a53c0-3266-4c84-b77f-85aa0df6e9ba".uuid),
                                                               AttributeFolderId("b470082a-9187-48e1-acfd-00000bad0000".uuid),
                                                               AttributeFolderId("b470082a-9187-48e1-acfd-80598f750d75".uuid))
        assert(executionResults.isSuccess)
        assert(executionResults()[0].isSuccess)
        assert(executionResults()[1].error == Result.Error(6108, "Attribute folder not found: \\\"b470082a-9187-48e1-acfd-00000bad0000\\\"."))
        assert(executionResults()[2].isSuccess)
    }
}