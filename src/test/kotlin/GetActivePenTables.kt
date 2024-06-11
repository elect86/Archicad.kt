import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetActivePenTables {

    val command = """|{
                     |    "command": "API.GetActivePenTables"
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "modelViewPenTableId": {
                      |            "attributeId": {
                      |                "guid": "638a0d7f-0d05-4909-8cbe-b22b2de9b7e6"
                      |            }
                      |        },
                      |        "layoutBookPenTableId": {
                      |            "attributeId": {
                      |                "guid": "638a0d7f-0d05-4909-8cbe-b22b2de9b7e6"
                      |            }
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad.activePenTables
        assert(result.isSuccess)
        val (modelView, layoutBook) = result()
        assert(modelView == AttributeId("638a0d7f-0d05-4909-8cbe-b22b2de9b7e6".uuid))
        assert(layoutBook == AttributeId("638a0d7f-0d05-4909-8cbe-b22b2de9b7e6".uuid))
    }
}