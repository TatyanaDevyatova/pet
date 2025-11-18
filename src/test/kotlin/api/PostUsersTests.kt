package api

import com.github.javafaker.Faker
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import utils.*

@Epic(Epics.API)
@Feature(Features.REGISTRATION)
class PostUsersTests {

    private lateinit var client: Client

    @BeforeClass
    fun setAll() {
        client = Client()
    }

    @Test(description = "Успешная регистрация с валидными данными")
    fun registerWithValidDataReturnsCreated() {
        val testEmail = Faker.instance().internet().emailAddress()
        val testUsername = Faker.instance().name().username()

        client.postUsers(
            postUsersBody = PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = testEmail,
                    password = Faker.instance().internet().password(),
                    username = testUsername
                )
            )
        ).apply {
            Assert.assertEquals(extract().statusLine(), CREATED_201)
            mapper.convertValue(extract().path("user"), UserDto::class.java).apply {
                Assert.assertEquals(username, testUsername)
                Assert.assertEquals(email, testEmail)
                Assert.assertEquals(bio, "")
                Assert.assertEquals(image, EXPECTED_IMAGE)
                Assert.assertTrue(token!!.matches(Regex("""token_[a-f0-9]{32}""")))
            }
        }
    }

    @DataProvider(name = "data")
    private fun getData(): Array<Array<Any?>> = arrayOf(
        arrayOf(
            PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = Faker.instance().internet().emailAddress(),
                    password = Faker.instance().internet().password(),
                    username = null
                )
            )
        ),
        arrayOf(
            PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = Faker.instance().internet().emailAddress(),
                    password = null,
                    username = Faker.instance().name().username()
                )
            )
        ),
        arrayOf(
            PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = null,
                    password = Faker.instance().internet().password(),
                    username = Faker.instance().name().username()
                )
            )
        )
    )

    @Test(
        description = "Регистрация с невалидными данными возвращает ошибку",
        dataProvider = "data"
    )
    fun registerWithInvalidDataReturnsError(postUsersBody: PostUsersBody) {
        client.postUsers(
            postUsersBody = postUsersBody
        ).apply {
            Assert.assertEquals(extract().statusLine(), ERROR_422)
        }
    }
}
