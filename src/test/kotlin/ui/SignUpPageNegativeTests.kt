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
import utils.REGISTER_PAGE_PATH

@Epic(Epics.UI)
@Feature(Features.SIGN_UP_PAGE)
class SignUpPageNegativeTests {

    private lateinit var driver: WebDriver
    private lateinit var signUpPage: SignUpPage

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "Если не введен пароль, должна быть заблокирована кнопка регистрации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldNotBeEnabledSignUpButtonWithoutPassword(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        signUpPage = SignUpPage(webDriver = driver)
        signUpPage.sendUsername(Faker.instance().name().username())
        signUpPage.sendEmail(Faker.instance().internet().emailAddress())
        Assert.assertFalse(signUpPage.checkSignUpButtonIsClickable())
    }

    @Test(
        description = "Если не введена электронная почта, должна быть заблокирована кнопка регистрации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldNotBeEnabledSignUpButtonWithoutEmail(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        signUpPage = SignUpPage(webDriver = driver)
        signUpPage.sendUsername(Faker.instance().name().username())
        signUpPage.sendPassword(Faker.instance().internet().password())
        Assert.assertFalse(signUpPage.checkSignUpButtonIsClickable())
    }

    @Test(
        description = "Если не введено имя пользователя, должна быть заблокирована кнопка регистрации",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldNotBeEnabledSignUpButtonWithoutUsername(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType, suffix = REGISTER_PAGE_PATH)
        signUpPage = SignUpPage(webDriver = driver)
        signUpPage.sendEmail(Faker.instance().internet().emailAddress())
        signUpPage.sendPassword(Faker.instance().internet().password())
        Assert.assertFalse(signUpPage.checkSignUpButtonIsClickable())
    }
}
