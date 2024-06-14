import ac26.Archicad26
import ac26.CloneProjectMapItemToViewMap
import ac26.NavigatorItemId
import ac26.uuid
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class `CloneProjectMapItemToViewMap test` {

    val command = """|{
                     |    "command": "API.CloneProjectMapItemToViewMap",
                     |    "parameters": {
                     |        "projectMapNavigatorItemId": {
                     |            "guid": "BB0F0CC3-23D7-422D-80A9-243FD993E3A3"
                     |        },
                     |        "parentNavigatorItemId": {
                     |            "guid": "3C4A2145-A752-401C-AD75-902D0235F1D7"
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
        val projectMapNavigatorItemId = NavigatorItemId("BB0F0CC3-23D7-422D-80A9-243FD993E3A3")
        val parentNavigatorItemId = NavigatorItemId("3C4A2145-A752-401C-AD75-902D0235F1D7")
        val createdNavigatorItemId = projectMapNavigatorItemId cloneTo parentNavigatorItemId
        assert(createdNavigatorItemId == NavigatorItemId("B6179C98-B27B-4C6E-B943-2346CF5DE106"))
    }
}