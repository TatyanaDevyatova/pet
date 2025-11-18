package api

import com.github.javafaker.Faker
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import utils.*

@Epic(Epics.API)
@Feature(Features.GETTING_ARTICLE)
class GetArticleBySlugTests {

    private lateinit var client: Client

    @BeforeClass
    fun setAll() {
        client = Client()
    }

    @Test(description = "Успешное получение статьи")
    fun requestWithValidSlugReturnArticleBySlug() {
        client.getArticleBySlug(slug = EXPECTED_SLUG).apply {
            Assert.assertEquals(extract().statusLine(), OK_200)
            mapper.convertValue(extract().path("article"), ArticleDto::class.java).apply {
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

    @Test(description = "Запрос с неверными данными возвращает ошибку")
    fun requestWithWrongSlugReturnError() {
        client.getArticleBySlug(slug = Faker.instance().lorem().word()).apply {
            Assert.assertEquals(extract().statusLine(), NOT_FOUND_404)
        }
    }
}
