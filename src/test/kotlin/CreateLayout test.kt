import ac26.Archicad26
import ac26.NavigatorItemId
import ac26.uuid
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class `CreateLayout test` {

    val command = """|{
                     |    "command": "API.CreateLayout",
                     |    "parameters": {
                     |        "layoutName": "Luke",
                     |        "masterNavigatorItemId": {
                     |            "guid": "592becd1-1c86-4637-8fd1-6ed44d4c6251"
                     |        },
                     |        "parentNavigatorItemId": {
                     |            "guid": "ecd23f92-5d0b-4e20-ac21-1700403e565e"
                     |        },
                     |        "layoutParameters": {
                     |            "horizontalSize": 600,
                     |            "verticalSize": 400,
                     |            "leftMargin": 10,
                     |            "topMargin": 10,
                     |            "rightMargin": 10,
                     |            "bottomMargin": 10,
                     |            "customLayoutNumber": "",
                     |            "customLayoutNumbering": false,
                     |            "doNotIncludeInNumbering": false,
                     |            "displayMasterLayoutBelow": false,
                     |            "layoutPageNumber": 1,
                     |            "actPageIndex": 1,
                     |            "currentRevisionId": "",
                     |            "currentFinalRevisionId": "",
                     |            "hasIssuedRevision": false,
                     |            "hasActualRevision": false
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "createdNavigatorItemId": {
                      |            "guid": "e3ffe769-431c-4f6b-a12e-a0fbfe321573"
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val createdNavigatorItemId = Archicad26.createLayout {
            name = "Luke"
            masterNavigatorItemId = NavigatorItemId("592becd1-1c86-4637-8fd1-6ed44d4c6251".uuid)
            parentNavigatorItemId = NavigatorItemId("ecd23f92-5d0b-4e20-ac21-1700403e565e".uuid)
            parameters {
                horizontalSize = 600
                verticalSize = 400
                leftMargin = 10
                topMargin = 10
                rightMargin = 10
                bottomMargin = 10
                customLayoutNumber = ""
                customLayoutNumbering = false
                doNotIncludeInNumbering = false
                displayMasterLayoutBelow = false
                layoutPageNumber = 1
                actPageIndex = 1
                currentRevisionId = ""
                currentFinalRevisionId = ""
                hasIssuedRevision = false
                hasActualRevision = false
            }
        }
        assert(createdNavigatorItemId.isSuccess)
        println(createdNavigatorItemId().guid)
        assert(createdNavigatorItemId() == NavigatorItemId("e3ffe769-431c-4f6b-a12e-a0fbfe321573".uuid))
    }
}