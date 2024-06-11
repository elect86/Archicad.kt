import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class Get3DBoundingBoxes {

    val command = """|{
                     |    "command": "API.Get3DBoundingBoxes",
                     |    "parameters": {
                     |        "elements": [
                     |            {
                     |                "elementId": {
                     |                    "guid": "0f7943ac-48fa-c74e-8ace-d1cb0db0dbb7"
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
                      |        "boundingBoxes3D": [
                      |            {
                      |                "boundingBox3D": {
                      |                    "xMin": 3.3,
                      |                    "yMin": 2.2,
                      |                    "zMin": 4.2,
                      |                    "xMax": 3.3,
                      |                    "yMax": 2.2,
                      |                    "zMax": 6.5
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
        val executionResults = Archicad.get3dBoundingBoxes(ElementId("0f7943ac-48fa-c74e-8ace-d1cb0db0dbb7".uuid),
                                                           ElementId("00000000-1111-2222-3333-444444444444".uuid))
        assert(executionResults.isSuccess)
        assert(executionResults()[0].isSuccess)
        assert(executionResults()[0]() == BoundingBoxes3D(3.3, 2.2, 4.2, 3.3, 2.2, 6.5))
        assert(executionResults()[1].isError)
        assert(executionResults()[1].error == Result.Error(7204, "The element does not exist."))
    }
}