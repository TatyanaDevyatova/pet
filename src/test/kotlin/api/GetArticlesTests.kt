package api

import com.fasterxml.jackson.core.type.TypeReference
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import utils.*

@Epic(Epics.API)
@Feature(Features.GETTING_ARTICLES)
class GetArticlesTests {

    private lateinit var client: Client

    @BeforeClass
    fun setAll() {
        client = Client()
    }

    @Test(description = "Успешное получение списка статей")
    fun requestReturnArticleList() {
        client.getArticles(limit = 10, offset = 0).apply {
            Assert.assertEquals(extract().statusLine(), OK_200)
            mapper.convertValue(extract().path("articles"), object : TypeReference<List<ArticleDto>>() {})[0].apply {
                Assert.assertEquals(slug, EXPECTED_SLUG)
                Assert.assertEquals(title, EXPECTED_TITLE)
                Assert.assertEquals(description, EXPECTED_DESCRIPTION)
                Assert.assertEquals(body, EXPECTED_BODY)
                Assert.assertEquals(tagList, EXPECTED_TAG_LIST)
                Assert.assertFalse(createdAt.isNullOrBlank())
                Assert.assertFalse(updatedAt.isNullOrBlank())
                Assert.assertFalse(favorited)
                Assert.assertEquals(favoritesCount, 2)
                Assert.assertEquals(author, EXPECTED_AUTHOR)
            }
        }
    }
}
