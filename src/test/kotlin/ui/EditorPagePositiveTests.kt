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
import utils.EDITOR_PAGE_PATH
import utils.Epics
import utils.Features
import kotlin.random.Random

@Epic(Epics.UI)
@Feature(Features.EDITOR_PAGE)
class EditorPagePositiveTests {

    private lateinit var driver: WebDriver
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
        description = "На странице редактора должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonEditorPageElements(driverType: DriverType) {
        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).clickNewArticleLink()
        assertPagePath(webDriver = driver, suffix = EDITOR_PAGE_PATH)
        EditorPage(webDriver = driver).checkCommonEditorPageElementsWasDisplayed()
    }

    @Test(
        description = "После ввода валидных данных должна быть произведена публикация статьи",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeSuccessPublicationWithValidData(driverType: DriverType) {
        val title = Faker.instance().lorem().word()

        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).clickNewArticleLink()
        val articlePage = EditorPage(webDriver = driver).fillEditorFieldsAndGetHomePage(
            articleTitle = title,
            description = Faker.instance().lorem().sentence(Random.nextInt(from = 3, until = 6)),
            body = Faker.instance().lorem().paragraph(Random.nextInt(from = 5, until = 9))
        )
        Assert.assertEquals(articlePage.getTitle(), title)
    }
}
