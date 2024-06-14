import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class DeleteNavigatorItems {

    val command = """|{
                     |    "command": "API.DeleteNavigatorItems",
                     |    "parameters": {
                     |        "navigatorItemIds": [
                     |            {
                     |                "navigatorItemId": {
                     |                    "guid": "ba224ea8-d8c0-46d5-948a-f8b19ad1b3fb"
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
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val executionResults = Archicad26.deleteNavigatorItems(NavigatorItemId("ba224ea8-d8c0-46d5-948a-f8b19ad1b3fb".uuid))
        assert(executionResults.isSuccess)
        assert(executionResults()[0].isSuccess)
    }
}