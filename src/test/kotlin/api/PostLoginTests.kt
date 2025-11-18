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
@Feature(Features.AUTHORIZATION)
class PostLoginTests {

    private lateinit var client: Client

    @BeforeClass
    fun setAll() {
        client = Client()
    }

    @Test(description = "Успешная авторизация с валидными данными")
    fun loginWithValidDataReturnsOk() {
        val testEmail = Faker.instance().internet().emailAddress()
        val testPassword = Faker.instance().internet().password()
        val testUsername = Faker.instance().name().username()
        ApiHelper.registerUser(
            client = client,
            postUsersBody = PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = testEmail,
                    password = testPassword,
                    username = testUsername
                )
            )
        )

        client.postLogin(
            postLoginBody = PostLoginBody(
                user = PostLoginBody.PostLogin(
                    email = testEmail,
                    password = testPassword
                )
            )
        ).apply {
            Assert.assertEquals(extract().statusLine(), OK_200)
            mapper.convertValue(extract().path("user"), UserDto::class.java).apply {
                Assert.assertEquals(username, testUsername)
                Assert.assertEquals(email, testEmail)
                Assert.assertEquals(bio, "")
                Assert.assertEquals(image, EXPECTED_IMAGE)
                Assert.assertTrue(token!!.matches(Regex("""token_[a-f0-9]{32}""")))
            }
        }
    }


    @Test(description = "Авторизация с неверными данными возвращает ошибку")
    fun loginWithWrongDataReturnsError() {
        client.postLogin(
            postLoginBody = PostLoginBody(
                user = PostLoginBody.PostLogin(
                    email = Faker.instance().internet().emailAddress(),
                    password = Faker.instance().internet().password()
                )
            )
        ).apply {
            Assert.assertEquals(extract().statusLine(), ERROR_401)
        }
    }

    @DataProvider(name = "data")
    private fun getData(): Array<Array<Any?>> = arrayOf(
        arrayOf(
            PostLoginBody(
                user = PostLoginBody.PostLogin(
                    email = Faker.instance().internet().emailAddress(),
                    password = null,
                )
            )
        ),
        arrayOf(
            PostLoginBody(
                user = PostLoginBody.PostLogin(
                    email = null,
                    password = Faker.instance().internet().password(),
                )
            )
        )
    )

    @Test(
        description = "Авторизация с невалидными данными возвращает ошибку",
        dataProvider = "data"
    )
    fun loginWithInvalidDataReturnsError(postLoginBody: PostLoginBody) {
        client.postLogin(postLoginBody = postLoginBody).apply {
            Assert.assertEquals(extract().statusLine(), ERROR_422)
        }
    }
}
