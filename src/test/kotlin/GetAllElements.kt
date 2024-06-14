import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllElements {

    val command = """|{
                     |    "command": "API.GetAllElements"
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "elements": [
                      |            {
                      |                "elementId": {
                      |                    "guid": "0f7943ac-48fa-c74e-8ace-d1cb0db0dbb7"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "63ba003e-4f4d-aa4a-8fbc-57379f2cdb98"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "6c7f82af-a362-4cfd-af06-3ca1668d92f1"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "65237b0f-73c3-4db3-9e94-3e1931f84aa0"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "5885e8f5-f67d-4cb9-987a-14ec733c8c27"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "6d21cbd5-eb75-4fc8-b20d-d4bb0470d827"
                      |                }
                      |            },
                      |            {
                      |                "elementId": {
                      |                    "guid": "23ccb727-2a40-4441-8789-0f7b9423a8fb"
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26.allElements
        assert(result.isSuccess)
        val elements = result()
        assert(elements.size == 7)
        assert(elements[0] == ElementId("0f7943ac-48fa-c74e-8ace-d1cb0db0dbb7".uuid))
        assert(elements[1] == ElementId("63ba003e-4f4d-aa4a-8fbc-57379f2cdb98".uuid))
        assert(elements[2] == ElementId("6c7f82af-a362-4cfd-af06-3ca1668d92f1".uuid))
        assert(elements[3] == ElementId("65237b0f-73c3-4db3-9e94-3e1931f84aa0".uuid))
        assert(elements[4] == ElementId("5885e8f5-f67d-4cb9-987a-14ec733c8c27".uuid))
        assert(elements[5] == ElementId("6d21cbd5-eb75-4fc8-b20d-d4bb0470d827".uuid))
        assert(elements[6] == ElementId("23ccb727-2a40-4441-8789-0f7b9423a8fb".uuid))
    }
}