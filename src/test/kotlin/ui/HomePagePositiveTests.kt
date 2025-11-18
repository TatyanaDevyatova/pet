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
import org.testng.annotations.Test
import utils.*

@Epic(Epics.UI)
@Feature(Features.HOME_PAGE)
class HomePagePositiveTests {

    private lateinit var driver: WebDriver

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "На домашней странице до авторизации должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonHomePageElementsWithoutAuthorization(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        HomePage(webDriver = driver).checkCommonHomePageElementsWasDisplayedWithoutAuthorization()
    }

    @Test(
        description = "На домашней странице после авторизации должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonHomePageElementsWithAuthorization(driverType: DriverType) {
        val email = Faker.instance().internet().emailAddress()
        val password = Faker.instance().internet().password()
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
        driver = authorizeUser(driverType = driverType, email = email, password = password)
        HomePage(webDriver = driver).checkCommonHomePageElementsWasDisplayedWithAuthorization()
    }

    @Test(
        description = "После клика на ссылку авторизации должна быть отображена страница авторизации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedSignInPageAfterClickOnSignInLink(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        HomePage(webDriver = driver).clickSignInLink()
        assertPagePath(webDriver = driver, suffix = LOGIN_PAGE_PATH)
    }

    @Test(
        description = "После клика на ссылку регистрации должна быть отображена страница регистрации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedSignUpPageAfterClickOnSignUpLink(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        HomePage(webDriver = driver).clickSignUpLink()
        assertPagePath(webDriver = driver, suffix = REGISTER_PAGE_PATH)
    }

    @Test(
        description = "После клика по тэгу должна быть отображена новая вкладка",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedJsTabAfterClickOnJsTag(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        val homePage = HomePage(webDriver = driver)
        homePage.clickJsTag()
        Assert.assertTrue(homePage.checkJsTabIsDisplayed())
    }

    @Test(
        description = "После клика на заголовок статьи должна быть отображена страница статьи",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedArticlePageAfterClickOnArticleTitle(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        val homePage = HomePage(webDriver = driver)
        val title = homePage.getArticleSnippetTitle()
        val articlePage = homePage.clickArticleSnippet()
        assertPagePath(
            webDriver = driver,
            suffix = ARTICLE_PAGE_PATH + title.replace(oldValue = " ", newValue = "-").lowercase()
        )
        Assert.assertEquals(articlePage.getTitle(), title)
    }
}
