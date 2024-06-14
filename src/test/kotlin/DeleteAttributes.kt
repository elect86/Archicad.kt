import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class DeleteAttributes {

    val command = """|{
                     |    "command": "API.DeleteAttributes",
                     |    "parameters": {
                     |        "attributeIds": [
                     |            {
                     |                "attributeId": {
                     |                    "guid": "7d30d4fc-4c93-4ffa-82c8-aa17b32da2b9"
                     |                }
                     |            },
                     |            {
                     |                "attributeId": {
                     |                    "guid": "b13d03c4-2388-4761-a8a3-ee9437ea9953"
                     |                }
                     |            },
                     |            {
                     |                "attributeId": {
                     |                    "guid": "d4cad19b-c68f-4534-bd7d-11c962df638a"
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
                      |                "success": true
                      |            },
                      |            {
                      |                "success": false,
                      |                "error": {
                      |                    "code": 6102,
                      |                    "message": "Attribute not found by guid (GUID: D4CAD19B-C68F-4534-BD7D-11C962DF638A)"
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val executionResults = Archicad26.deleteAttributes(AttributeId("7d30d4fc-4c93-4ffa-82c8-aa17b32da2b9".uuid),
                                                           AttributeId("b13d03c4-2388-4761-a8a3-ee9437ea9953".uuid),
                                                           AttributeId("d4cad19b-c68f-4534-bd7d-11c962df638a".uuid))
        assert(executionResults.isSuccess)
        assert(executionResults()[0].isSuccess)
        assert(executionResults()[1].isSuccess)
        assert(executionResults()[2].failure == Response.Failure(6102, "Attribute not found by guid (GUID: D4CAD19B-C68F-4534-BD7D-11C962DF638A)"))
    }
}