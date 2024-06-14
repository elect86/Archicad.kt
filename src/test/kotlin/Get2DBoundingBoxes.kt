import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class Get2DBoundingBoxes {

    val command = """|{
                     |    "command": "API.Get2DBoundingBoxes",
                     |    "parameters": {
                     |        "elements": [
                     |            {
                     |                "elementId": {
                     |                    "guid": "63ba003e-4f4d-aa4a-8fbc-57379f2cdb98"
                     |                }
                     |            },
                     |            {
                     |                "elementId": {
                     |                    "guid": "00000000-1111-2222-3333-444444444444"
                     |                }
                     |            }
                     |        ]
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "boundingBoxes2D": [
                      |            {
                      |                "boundingBox2D": {
                      |                    "xMin": 3.3,
                      |                    "yMin": 2.2,
                      |                    "xMax": 3.3,
                      |                    "yMax": 2.2
                      |                }
                      |            },
                      |            {
                      |                "error": {
                      |                    "code": 7204,
                      |                    "message": "The element does not exist."
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val executionResults = Archicad26.get2dBoundingBoxes(ElementId("63ba003e-4f4d-aa4a-8fbc-57379f2cdb98".uuid),
                                                             ElementId("00000000-1111-2222-3333-444444444444".uuid))
        assert(executionResults.isSuccess)
        assert(executionResults()[0].isSuccess)
        assert(executionResults()[0]() == BoundingBoxes2D(3.3, 2.2, 3.3, 2.2))
        assert(executionResults()[1].isFailure)
        assert(executionResults()[1].failure == Response.Failure(7204, "The element does not exist."))
    }
}