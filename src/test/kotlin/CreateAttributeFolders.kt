import ac26.Archicad26
import ac26.AttributeType
import ac26.plus
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class CreateAttributeFolders {

    val command = """|{
                     |    "command": "API.CreateAttributeFolders",
                     |    "parameters": {
                     |        "attributeFolders": [
                     |            {
                     |                "attributeType": "Surface",
                     |                "path": [
                     |                    "  InvalidPath "
                     |                ]
                     |            },
                     |            {
                     |                "attributeType": "Surface",
                     |                "path": [
                     |                    "ValidPath"
                     |                ]
                     |            },
                     |            {
                     |                "attributeType": "ZoneCategory",
                     |                "path": [
                     |                    "A",
                     |                    "AA"
                     |                ]
                     |            }
                     |        ]
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "executionResults": [
                      |            {
                      |                "success": false,
                      |                "error": {
                      |                    "code": 6107,
                      |                    "message": "Illegal attribute folder name: \"  InvalidPath \"."
                      |                }
                      |            },
                      |            {
                      |                "success": true
                      |            },
                      |            {
                      |                "success": false,
                      |                "error": {
                      |                    "code": 6110,
                      |                    "message": "Folder operations not enabled for attribute type \"Lines\""
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val results = Archicad26.createAttributeFolders(AttributeType.Surface + "  InvalidPath ",
                                                        AttributeType.Surface + "ValidPath",
                                                        AttributeType.ZoneCategory + listOf("A", "AA"))
        assert(results.size == 3)
        assert(results[0].failure == Response.Failure(6107, "Illegal attribute folder name: \"  InvalidPath \"."))
        assert(results[1].isSuccess)
        assert(results[2].failure == Response.Failure(6110, "Folder operations not enabled for attribute type \"Lines\""))
    }
}