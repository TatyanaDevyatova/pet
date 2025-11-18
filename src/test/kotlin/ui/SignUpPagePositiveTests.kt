package ui

import com.github.javafaker.Faker
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.openqa.selenium.WebDriver
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import utils.*

@Epic(Epics.UI)
@Feature(Features.SIGN_UP_PAGE)
class SignUpPagePositiveTests {

    private lateinit var driver: WebDriver

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "На странице регистрации должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonSignUpPageElements(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        SignUpPage(webDriver = driver).checkCommonSignUpPageElementsWasDisplayed()
    }

    @Test(
        description = "После клика на ссылку авторизации должна быть отображена страница авторизации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedSignInPageAfterClickOnLoginLink(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        SignUpPage(webDriver = driver).clickLoginLink()
        assertPagePath(webDriver = driver, suffix = LOGIN_PAGE_PATH)
    }

    @Test(
        description = "После ввода валидных данных, должна быть произведена регистрация",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeSuccessRegistrationWithValidData(driverType: DriverType) {
        val username = Faker.instance().name().username()

        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        val signUpPage = SignUpPage(webDriver = driver)
        Assert.assertFalse(signUpPage.checkSignUpButtonIsClickable())
        val mainPage = signUpPage.fillSignUpFieldsAndGetHomePage(
            username = username,
            email = Faker.instance().internet().emailAddress(),
            password = Faker.instance().internet().password()
        )
        Assert.assertEquals(mainPage.getProfileLink(), BASE_PAGE_URL + PROFILE_PAGE_PATH + username)
    }
}
