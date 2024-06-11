import ac26.Archicad
import ac26.NavigatorItemId
import ac26.NumberingStyle
import ac26.uuid
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class CreateLayoutSubset {

    val command = """|{
                     |    "command": "API.CreateLayoutSubset",
                     |    "parameters": {
                     |        "parentNavigatorItemId": {
                     |            "guid": "ecd23f92-5d0b-4e20-ac21-1700403e565e"
                     |        },
                     |        "subsetParameters": {
                     |            "name": "MySubset",
                     |            "includeToIDSequence": true,
                     |            "customNumbering": false,
                     |            "continueNumbering": true,
                     |            "useUpperPrefix": true,
                     |            "addOwnPrefix": true,
                     |            "customNumber": "CN",
                     |            "autoNumber": "AN",
                     |            "numberingStyle": "ABC",
                     |            "startAt": 0,
                     |            "ownPrefix": "MyPrefix"
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "createdSubsetId": {
                      |            "guid": "be917cff-6a1f-47b0-87dd-10e06395a2a5"
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val createdSubsetId = Archicad.createLayoutSubset {
            parentNavigatorItemId = NavigatorItemId("ecd23f92-5d0b-4e20-ac21-1700403e565e".uuid)
            parameters {
                name = "MySubset"
                includeToIDSequence = true
                customNumbering = false
                continueNumbering = true
                useUpperPrefix = true
                addOwnPrefix = true
                customNumber = "CN"
                autoNumber = "AN"
                numberingStyle = NumberingStyle.ABC
                startAt = 0
                ownPrefix = "MyPrefix"
            }
        }
        assert(createdSubsetId.isSuccess)
        assert(createdSubsetId() == NavigatorItemId("be917cff-6a1f-47b0-87dd-10e06395a2a5".uuid))
    }
}