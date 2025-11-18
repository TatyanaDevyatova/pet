package ui

import com.github.javafaker.Faker
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.openqa.selenium.WebDriver
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import utils.Epics
import utils.Features
import utils.LOGIN_PAGE_PATH

@Epic(Epics.UI)
@Feature(Features.SIGN_IN_PAGE)
class SignInPageNegativeTests {

    private lateinit var driver: WebDriver
    private lateinit var signInPage: SignInPage

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "Если не введен пароль, должна быть заблокирована кнопка авторизации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldNotBeEnabledSignInButtonWithoutPassword(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        signInPage.sendEmail(Faker.instance().internet().emailAddress())
        Assert.assertFalse(signInPage.checkSignInButtonIsClickable())
    }

    @Test(
        description = "Если не введена электронная почта, должна быть заблокирована кнопка авторизации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldNotBeEnabledSignInButtonWithoutEmail(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        signInPage.sendPassword(Faker.instance().internet().password())
        Assert.assertFalse(signInPage.checkSignInButtonIsClickable())
    }

    @Test(
        description = "Если неверные данные, должна быть заблокирована кнопка авторизации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedErrorWithInvalidData(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
        signInPage = SignInPage(webDriver = driver)
        signInPage.sendEmail(Faker.instance().internet().emailAddress())
        signInPage.sendPassword(Faker.instance().internet().password())
        signInPage.clickSignInButton()
        Assert.assertTrue(signInPage.checkErrorWasDisplayed())
    }
}
