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
@Feature(Features.SIGN_IN_PAGE)
class SignInPagePositiveTests {

    private lateinit var driver: WebDriver
    private lateinit var signInPage: SignInPage

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "На странице авторизации должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonSignInPageElements(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        assertPagePath(webDriver = driver, suffix = LOGIN_PAGE_PATH)
        signInPage.checkCommonSignInPageElementsWasDisplayed()
    }

    @Test(
        description = "После клика на ссылку регистрации должна быть отображена страница регистрации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedSignUpPageAfterClickOnRegisterLink(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        signInPage.clickRegisterLink()
        assertPagePath(webDriver = driver, suffix = REGISTER_PAGE_PATH)
    }

    @Test(
        description = "После ввода валидных данных должна быть произведена авторизация",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeSuccessAuthorizationWithValidData(driverType: DriverType) {
        val username = Faker.instance().name().username()
        val email = Faker.instance().internet().emailAddress()
        val password = Faker.instance().internet().password()
        ApiHelper.registerUser(
            client = Client(),
            postUsersBody = PostUsersBody(
                user = PostUsersBody.PostUsers(
                    email = email,
                    password = password,
                    username = username
                )
            )
        )

        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        Assert.assertFalse(signInPage.checkSignInButtonIsClickable())
        val mainPage = signInPage.fillSignInFieldsAndGetHomePage(
            email = email,
            password = password
        )
        Assert.assertEquals(mainPage.getProfileLink(), BASE_PAGE_URL + PROFILE_PAGE_PATH + username)
    }
}
