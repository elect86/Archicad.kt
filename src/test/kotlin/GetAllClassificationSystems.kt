import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllClassificationSystems {

    val command = """|{
                     |    "command": "API.GetAllClassificationSystems"
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "classificationSystems": [
                      |            {
                      |                "classificationSystemId": {
                      |                    "guid": "632c30e5-2b82-4a75-bfad-d52febce9a4a"
                      |                },
                      |                "name": "Archicad Classification",
                      |                "description": "",
                      |                "source": "www.graphisoft.com",
                      |                "version": "v 2.0",
                      |                "date": "2019-03-15"
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26.allClassificationSystem
        assert(result.isSuccess)
        val systems = result()[0]
        assert(systems.classificationSystemId == ClassificationSystemId("632c30e5-2b82-4a75-bfad-d52febce9a4a".uuid))
        assert(systems.name == "Archicad Classification")
        assert(systems.description.isEmpty())
        assert(systems.source == "www.graphisoft.com")
        assert(systems.version == "v 2.0")
        println(systems.date.toString() == "2019-03-15")
    }
}