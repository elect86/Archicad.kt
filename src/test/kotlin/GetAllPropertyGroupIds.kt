import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllPropertyGroupIds {

    val command = """|{
                     |    "command": "API.GetAllPropertyGroupIds",
                     |    "parameters": {
                     |        "propertyType": "UserDefined"
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "propertyGroupIds": [
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "B12F472E-419D-417E-9A76-85EA50A72109"
                      |                }
                      |            },
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "DD02E8F4-C293-487A-A294-6660A5833727"
                      |                }
                      |            },
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "D1219C84-EB08-4478-BD05-90495552554A"
                      |                }
                      |            },
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "DD952395-13B6-4121-AB97-6C8BD85B44EC"
                      |                }
                      |            },
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "D912ACA9-C082-4B79-A7F3-24151CBD1AD9"
                      |                }
                      |            },
                      |            {
                      |                "propertyGroupId": {
                      |                    "guid": "6EC37DFB-BA47-2041-B04B-703ACB89BF7E"
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26 getAllPropertyGroupIds "UserDefined"
        assert(result.isSuccess)
        val elements = result()
        assert(elements.size == 6)
        assert(elements[0] == PropertyGroupId("B12F472E-419D-417E-9A76-85EA50A72109".uuid))
        assert(elements[1] == PropertyGroupId("DD02E8F4-C293-487A-A294-6660A5833727".uuid))
        assert(elements[2] == PropertyGroupId("D1219C84-EB08-4478-BD05-90495552554A".uuid))
        assert(elements[3] == PropertyGroupId("DD952395-13B6-4121-AB97-6C8BD85B44EC".uuid))
        assert(elements[4] == PropertyGroupId("D912ACA9-C082-4B79-A7F3-24151CBD1AD9".uuid))
        assert(elements[5] == PropertyGroupId("6EC37DFB-BA47-2041-B04B-703ACB89BF7E".uuid))
    }
}