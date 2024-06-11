import ac26.NavigatorItemId
import ac26.uuid
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class CloneProjectMapItemToViewMap {

    val command = """|{
                     |    "command": "API.CloneProjectMapItemToViewMap",
                     |    "parameters": {
                     |        "projectMapNavigatorItemId": {
                     |            "guid": "bb0f0cc3-23d7-422d-80a9-243fd993e3a3"
                     |        },
                     |        "parentNavigatorItemId": {
                     |            "guid": "3c4a2145-a752-401c-ad75-902d0235f1d7"
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "createdNavigatorItemId": {
                      |            "guid": "b6179c98-b27b-4c6e-b943-2346cf5de106"
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val projectMapNavigatorItemId = NavigatorItemId("BB0F0CC3-23D7-422D-80A9-243FD993E3A3".uuid)
        val parentNavigatorItemId = NavigatorItemId("3C4A2145-A752-401C-AD75-902D0235F1D7".uuid)
        val createdNavigatorItemId = projectMapNavigatorItemId cloneTo parentNavigatorItemId
        assert(createdNavigatorItemId().guid == "B6179C98-B27B-4C6E-B943-2346CF5DE106".uuid)
    }
}