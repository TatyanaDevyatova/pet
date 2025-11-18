package ui

import api.ApiHelper
import api.Client
import api.PostUsersBody
import com.github.javafaker.Faker
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.openqa.selenium.WebDriver
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import utils.Epics
import utils.Features
import kotlin.random.Random

@Epic(Epics.UI)
@Feature(Features.EDITOR_PAGE)
class EditorPageNegativeTests {

    private lateinit var driver: WebDriver
    private lateinit var editorPage: EditorPage
    private lateinit var email: String
    private lateinit var password: String

    @BeforeMethod
    fun setUp() {
        email = Faker.instance().internet().emailAddress()
        password = Faker.instance().internet().password()
        ApiHelper.registerUser(
            client = Client(),
            postUsersBody = PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = email,
                    password = password,
                    username = Faker.instance().name().username()
                )
            )
        )
    }

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "Если не введен заголовок статьи, должна быть отображена ошибка",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedErrorWithoutTitle(driverType: DriverType) {
        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).clickNewArticleLink()
        editorPage = EditorPage(webDriver = driver)
        editorPage.sendDescription(Faker.instance().lorem().sentence(Random.nextInt(from = 3, until = 6)))
        editorPage.sendBody(Faker.instance().lorem().paragraph(Random.nextInt(from = 5, until = 9)))
        editorPage.clickPublishButton()
        Assert.assertTrue(editorPage.checkErrorWasDisplayed())
    }

    @Test(
        description = "Если не введено описание статьи, должна быть отображена ошибка",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedErrorWithoutDescription(driverType: DriverType) {
        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).clickNewArticleLink()
        editorPage = EditorPage(webDriver = driver)
        editorPage.sendTitle(Faker.instance().lorem().word())
        editorPage.sendBody(Faker.instance().lorem().paragraph(Random.nextInt(from = 5, until = 9)))
        editorPage.clickPublishButton()
        Assert.assertTrue(editorPage.checkErrorWasDisplayed())
    }

    @Test(
        description = "Если не введен текст статьи, должна быть отображена ошибка",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedErrorWithoutBody(driverType: DriverType) {
        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).clickNewArticleLink()
        editorPage = EditorPage(webDriver = driver)
        editorPage.sendTitle(Faker.instance().lorem().word())
        editorPage.sendDescription(Faker.instance().lorem().sentence(Random.nextInt(from = 3, until = 6)))
        editorPage.clickPublishButton()
        Assert.assertTrue(editorPage.checkErrorWasDisplayed())
    }
}
