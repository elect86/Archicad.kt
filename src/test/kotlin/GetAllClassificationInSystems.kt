import ac26.*
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class GetAllClassificationInSystems {

    val command = """|{
                     |    "command": "API.GetAllClassificationInSystems",
                     |    "parameters": {
                     |        "classificationSystemId": {
                     |            "guid": "632c30e5-2b82-4a75-bfad-d52febce9a4a"
                     |        }
                     |    }
                     |}""".trimMargin()
    val response = """|{
                      |    "succeeded": true,
                      |    "result": {
                      |        "classificationItems": [
                      |            {
                      |                "classificationItem": {
                      |                    "classificationItemId": {
                      |                        "guid": "daa21b11-88b9-4897-a12e-44cd2e3d07cd"
                      |                    },
                      |                    "id": "Site",
                      |                    "name": "",
                      |                    "description": "",
                      |                    "children": [
                      |                        {
                      |                            "classificationItem": {
                      |                                "classificationItemId": {
                      |                                    "guid": "0ee23e04-f4fc-4daa-ae52-01a3b0503050"
                      |                                },
                      |                                "id": "Geographic Element",
                      |                                "name": "",
                      |                                "description": "",
                      |                                "children": [
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "645b2def-afa4-4028-b432-f3bce73ae26a"
                      |                                            },
                      |                                            "id": "Terrain",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    },
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "81607a0b-009b-4dbf-9ddd-db8018525506"
                      |                                            },
                      |                                            "id": "Site Geometry",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    },
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "dc58C1e4-19b2-43b3-8f14-75e3bf9ac375"
                      |                                            },
                      |                                            "id": "Flora & Fauna",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    }
                      |                                ]
                      |                            }
                      |                        },
                      |                        {
                      |                            "classificationItem": {
                      |                                "classificationItemId": {
                      |                                    "guid": "d54435c-648b-478b-95a6-8f95603fadeb"
                      |                                },
                      |                                "id": "Massing",
                      |                                "name": "",
                      |                                "description": "",
                      |                                "children": [
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "8c3abc5e-1b21-40e4-9d30-d5766e4d30b6"
                      |                                            },
                      |                                            "id": "Morph",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    }
                      |                                ]
                      |                            }
                      |                        },
                      |                        {
                      |                            "classificationItem": {
                      |                                "classificationItemId": {
                      |                                    "guid": "ce1fd4cb-b232-4ade-adbb-533704a2da66"
                      |                                },
                      |                                "id": "Civil Element",
                      |                                "name": "",
                      |                                "description": ""
                      |                            }
                      |                        }
                      |                    ]
                      |                }
                      |            },
                      |            {
                      |                "classificationItem": {
                      |                    "classificationItemId": {
                      |                        "guid": "5352c474-dc99-4da5-a77B-e001cb90bf5e"
                      |                    },
                      |                    "id": "Elements",
                      |                    "name": "",
                      |                    "description": "",
                      |                    "children": [
                      |                        {
                      |                            "classificationItem": {
                      |                                "classificationItemId": {
                      |                                    "guid": "8b729ac6-15b1-4f74-8b4f-359d80da6871"
                      |                                },
                      |                                "id": "Construction Element",
                      |                                "name": "",
                      |                                "description": "",
                      |                                "children": [
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "1aeb79eb-8d88-4f2e-9ea5-3432d006ab4f"
                      |                                            },
                      |                                            "id": "Wall",
                      |                                            "name": "",
                      |                                            "description": "",
                      |                                            "children": [
                      |                                                {
                      |                                                    "classificationItem": {
                      |                                                        "classificationItemId": {
                      |                                                            "guid": "232f57c7-3ad2-4cff-a0ee-694b1a3a5cda"
                      |                                                        },
                      |                                                        "id": "Polygonal Wall",
                      |                                                        "name": "",
                      |                                                        "description": ""
                      |                                                    }
                      |                                                },
                      |                                                {
                      |                                                    "classificationItem": {
                      |                                                        "classificationItemId": {
                      |                                                            "guid": "dae7388f-0133-4667-a772-49170bc9f837"
                      |                                                        },
                      |                                                        "id": "Solid Wall",
                      |                                                        "name": "",
                      |                                                        "description": ""
                      |                                                    }
                      |                                                },
                      |                                                {
                      |                                                    "classificationItem": {
                      |                                                        "classificationItemId": {
                      |                                                            "guid": "d74432d3-d889-41be-ba87-dc128ae430b4"
                      |                                                        },
                      |                                                        "id": "Standard Wall",
                      |                                                        "name": "",
                      |                                                        "description": ""
                      |                                                    }
                      |                                                }
                      |                                            ]
                      |                                        }
                      |                                    },
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "31dd3189-11d1-4d7b-a53a-f0606adeda47"
                      |                                            },
                      |                                            "id": "Column",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    },
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "bf3d655e-4ec7-428b-be51-59de113a1078"
                      |                                            },
                      |                                            "id": "Beam",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    },
                      |                                    {
                      |                                        "classificationItem": {
                      |                                            "classificationItemId": {
                      |                                                "guid": "b5a21b5f-b35b-41b9-bfe4-cc2acb496ee5"
                      |                                            },
                      |                                            "id": "Slab",
                      |                                            "name": "",
                      |                                            "description": ""
                      |                                        }
                      |                                    }
                      |                                ]
                      |                            }
                      |                        }
                      |                    ]
                      |                }
                      |            }
                      |        ]
                      |    }
                      |}""".trimMargin()

    @BeforeTest
    fun setupClient() = mock(command, response)

    @Test
    fun run() {
        val result = Archicad26 getAllClassificationInSystem ClassificationSystemId("632c30e5-2b82-4a75-bfad-d52febce9a4a".uuid)
        assert(result.isSuccess)
        val (a, b) = result().apply {
            assert(size == 2)
        }
        a.apply {
            assert(classificationItemId == ClassificationItemId("daa21b11-88b9-4897-a12e-44cd2e3d07cd".uuid))
            assert(id == "Site")
            assert(name.isEmpty())
            assert(description.isEmpty())
            assert(children.size == 3)
            val (aa, ab, ac) = children
            aa.apply {
                assert(classificationItemId == ClassificationItemId("0ee23e04-f4fc-4daa-ae52-01a3b0503050".uuid))
                assert(id == "Geographic Element")
                assert(name.isEmpty())
                assert(description.isEmpty())
                assert(children.size == 3)
                val (aaa, aab, aac) = children
                aaa.apply {
                    assert(classificationItemId == ClassificationItemId("645b2def-afa4-4028-b432-f3bce73ae26a".uuid))
                    assert(id == "Terrain")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
                aab.apply {
                    assert(classificationItemId == ClassificationItemId("81607a0b-009b-4dbf-9ddd-db8018525506".uuid))
                    assert(id == "Site Geometry")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
                aac.apply {
                    assert(classificationItemId == ClassificationItemId("dc58C1e4-19b2-43b3-8f14-75e3bf9ac375".uuid))
                    assert(id == "Flora & Fauna")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
            }
            ab.apply {
                assert(classificationItemId == ClassificationItemId("d54435c-648b-478b-95a6-8f95603fadeb".uuid))
                assert(id == "Massing")
                assert(name.isEmpty())
                assert(description.isEmpty())
                assert(children.size == 1)
                children[0].apply {
                    assert(classificationItemId == ClassificationItemId("8c3abc5e-1b21-40e4-9d30-d5766e4d30b6".uuid))
                    assert(id == "Morph")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
            }
            ac.apply {
                assert(classificationItemId == ClassificationItemId("ce1fd4cb-b232-4ade-adbb-533704a2da66".uuid))
                assert(id == "Civil Element")
                assert(name.isEmpty())
                assert(description.isEmpty())
                assert(children.isEmpty())
            }
        }
        b.apply {
            assert(classificationItemId == ClassificationItemId("5352c474-dc99-4da5-a77B-e001cb90bf5e".uuid))
            assert(id == "Elements")
            assert(name.isEmpty())
            assert(description.isEmpty())
            assert(children.size == 1)
            children[0].apply {
                assert(classificationItemId == ClassificationItemId("8b729ac6-15b1-4f74-8b4f-359d80da6871".uuid))
                assert(id == "Construction Element")
                assert(name.isEmpty())
                assert(description.isEmpty())
                assert(children.size == 4)
                val (baa, bab, bac, bad) = children
                baa.apply {
                    assert(classificationItemId == ClassificationItemId("1aeb79eb-8d88-4f2e-9ea5-3432d006ab4f".uuid))
                    assert(id == "Wall")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.size == 3)
                    val (baaa, baab, baac) = children
                    baaa.apply {
                        assert(classificationItemId == ClassificationItemId("232f57c7-3ad2-4cff-a0ee-694b1a3a5cda".uuid))
                        assert(id == "Polygonal Wall")
                        assert(name.isEmpty())
                        assert(description.isEmpty())
                        assert(children.isEmpty())
                    }
                    baab.apply {
                        assert(classificationItemId == ClassificationItemId("dae7388f-0133-4667-a772-49170bc9f837".uuid))
                        assert(id == "Solid Wall")
                        assert(name.isEmpty())
                        assert(description.isEmpty())
                        assert(children.isEmpty())
                    }
                    baac.apply {
                        assert(classificationItemId == ClassificationItemId("d74432d3-d889-41be-ba87-dc128ae430b4".uuid))
                        assert(id == "Standard Wall")
                        assert(name.isEmpty())
                        assert(description.isEmpty())
                        assert(children.isEmpty())
                    }
                }
                bab.apply {
                    assert(classificationItemId == ClassificationItemId("31dd3189-11d1-4d7b-a53a-f0606adeda47".uuid))
                    assert(id == "Column")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
                bac.apply {
                    assert(classificationItemId == ClassificationItemId("bf3d655e-4ec7-428b-be51-59de113a1078".uuid))
                    assert(id == "Beam")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
                bad.apply {
                    assert(classificationItemId == ClassificationItemId("b5a21b5f-b35b-41b9-bfe4-cc2acb496ee5".uuid))
                    assert(id == "Slab")
                    assert(name.isEmpty())
                    assert(description.isEmpty())
                    assert(children.isEmpty())
                }
            }
        }
    }
}