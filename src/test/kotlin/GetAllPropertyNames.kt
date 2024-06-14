import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllPropertyNames {

    val command = """|{
                     |    "command": "API.GetAllPropertyNames"
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "properties": [
                      |            {
                      |                "type": "BuiltIn",
                      |                "nonLocalizedName": "WindowDoor_OpeningVolume"
                      |            },
                      |            {
                      |                "type": "UserDefined",
                      |                "localizedName": [
                      |                    "GENERAL RATINGS",
                      |                    "Combustible"
                      |                ]
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26.allPropertyNames
        assert(result.isSuccess)
        val names = result()
        assert(names.size == 2)
        val (a, b) = names
        assert(a is BuildInProperty && a.nonLocalizedName == "WindowDoor_OpeningVolume")
        assert(b is UserDefinedInProperty && b.group == "GENERAL RATINGS" && b.localizedName == "Combustible")
    }
}