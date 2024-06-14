import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class ExecuteAddOnCommand {

    val command = """|{
                     |    "command": "API.ExecuteAddOnCommand",
                     |    "parameters": {
                     |        "addOnCommandId": {
                     |            "commandNamespace": "MyAddOnCommandNamespace",
                     |            "commandName": "MyAddOnCommandName"
                     |        },
                     |        "addOnCommandParameters": {
                     |            "myAddOnCommandParameter1": "X=?"
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "addOnCommandResponse": {
                      |            "myAddOnCommandResult1": 42
                      |        }
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val executionResults = Archicad26.executeAddOnCommand {
            commandNamespace = "MyAddOnCommandNamespace"
            commandName = "MyAddOnCommandName"
            parameters("myAddOnCommandParameter1" to "X=?")
        }
        assert(executionResults.isSuccess)
        assert(executionResults().first == "myAddOnCommandResult1")
        assert(executionResults().second == "42")
    }
}