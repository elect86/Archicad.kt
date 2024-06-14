import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllPropertyIdsOfElements {

    val command = """|{
                     |    "command": "API.GetAllPropertyIdsOfElements",
                     |    "parameters": {
                     |        "elements": [
                     |            {
                     |                "elementId": {
                     |                    "guid": "63ba003e-4f4d-aa4a-8fbc-57379f2cdb98"
                     |                }
                     |            },
                     |            {
                     |                "elementId": {
                     |                    "guid": "11111111-2222-3333-4444-555555555555"
                     |                }
                     |            }
                     |        ],
                     |        "propertyType": "UserDefined"
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "propertyIdsOfElements": [
                      |            {
                      |                "propertyIdsOfElement": {
                      |                    "elementId": {
                      |                        "guid": "63BA003E-4F4D-AA4A-8FBC-57379F2CDB98"
                      |                    },
                      |                    "propertyIds": [
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "E480E81E-EDE3-43FC-9C52-B55A4CA1A85C"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "13A61253-66A9-4494-9393-9E8F2E19D55E"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "BCB5813F-2115-4B8B-A12F-16CFE37C7B7F"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "6F4A46AC-AE91-47E6-BF4A-9F9AB01A4986"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "F6F67733-1DC1-442A-8CF4-ACD2DF7E62C6"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "52D7923A-E5D7-47DF-9319-834B2CB68A6C"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "331D26A3-8168-460C-B7F5-0FA11B596B60"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "78B73923-1B87-460B-8D9E-6E3041CF38D6"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "2FAB57AB-40D6-4B7B-A7F7-31FAE42BCFBD"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "3D9EF415-8D5E-42C3-999F-3CE138DF341F"
                      |                            }
                      |                        },
                      |                        {
                      |                            "propertyId": {
                      |                                "guid": "9CC16F4D-9754-B744-B3F8-20BA074A3B2D"
                      |                            }
                      |                        }
                      |                    ]
                      |                }
                      |            },
                      |            {
                      |                "error": {
                      |                    "code": 7204,
                      |                    "message": "Element not found (element guid: \"11111111-2222-3333-4444-555555555555\")"
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26.getAllPropertyIdsOfElements(ElementId("63BA003E-4F4D-AA4A-8FBC-57379F2CDB98".uuid),
                                                            ElementId("11111111-2222-3333-4444-555555555555".uuid),
                                                            propertyType = "UserDefined")
        assert(result.isSuccess)
        val elements = result()
        assert(elements.size == 2)
        val (a, b) = elements
        assert(a.isSuccess)
        val ids = a()
        assert(ids.size == 11)
        assert(ids[0] == PropertyId("E480E81E-EDE3-43FC-9C52-B55A4CA1A85C".uuid))
        assert(ids[1] == PropertyId("13A61253-66A9-4494-9393-9E8F2E19D55E".uuid))
        assert(ids[2] == PropertyId("BCB5813F-2115-4B8B-A12F-16CFE37C7B7F".uuid))
        assert(ids[3] == PropertyId("6F4A46AC-AE91-47E6-BF4A-9F9AB01A4986".uuid))
        assert(ids[4] == PropertyId("F6F67733-1DC1-442A-8CF4-ACD2DF7E62C6".uuid))
        assert(ids[5] == PropertyId("52D7923A-E5D7-47DF-9319-834B2CB68A6C".uuid))
        assert(ids[6] == PropertyId("331D26A3-8168-460C-B7F5-0FA11B596B60".uuid))
        assert(ids[7] == PropertyId("78B73923-1B87-460B-8D9E-6E3041CF38D6".uuid))
        assert(ids[8] == PropertyId("2FAB57AB-40D6-4B7B-A7F7-31FAE42BCFBD".uuid))
        assert(ids[9] == PropertyId("3D9EF415-8D5E-42C3-999F-3CE138DF341F".uuid))
        assert(ids[10] == PropertyId("9CC16F4D-9754-B744-B3F8-20BA074A3B2D".uuid))
    }
}