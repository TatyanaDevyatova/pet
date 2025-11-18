package api

import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import utils.Epics
import utils.Features
import utils.OK_200

@Epic(Epics.API)
@Feature(Features.GETTING_TAGS)
class GetTagsTests {

    private lateinit var client: Client
    private val tags = listOf(
        "ai", "api", "architecture", "backend", "beginners", "datascience", "frontend", "hooks", "javascript",
        "machinelearning", "nodejs", "programming", "python", "react", "webdev"
    ).sorted()

    @BeforeClass
    fun setAll() {
        client = Client()
    }

    @Test(description = "Успешное получение списка тэгов")
    fun requestReturnTagList() {
        client.getTags().apply {
            Assert.assertEquals(extract().statusLine(), OK_200)
            Assert.assertEquals(this.extract().jsonPath().getList("tags", String::class.java).sorted(), tags)
        }
    }
}
