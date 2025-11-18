package ui

import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.annotations.DataProvider
import utils.BASE_PAGE_URL
import utils.COMMON_TIMEOUT
import utils.LOGIN_PAGE_PATH

enum class DriverType {
    CHROME, FIREFOX, EDGE;
}

object DriverDataProviders {

    @DataProvider(name = "driverData")
    @JvmStatic
    fun getData(): Array<Array<DriverType>> = DriverType.values().map { arrayOf(it) }.toTypedArray()
}

@Step("Получить вебдрайвер для теста в зависимости от типа браузера")
fun getWebDriverForTest(driverType: DriverType): WebDriver {
    val webDriver: WebDriver = when (driverType) {
        DriverType.CHROME -> ChromeDriver(ChromeOptions().addArguments("--headless"))
        DriverType.FIREFOX -> FirefoxDriver(FirefoxOptions().addArguments("--headless"))
        DriverType.EDGE -> EdgeDriver(EdgeOptions().addArguments("--headless"))
    }
    webDriver.manage().window().maximize()
    return webDriver
}

@Step("Открыть страницу для теста")
fun openPageForTest(webDriver: WebDriver, suffix: String = "") {
    webDriver.get(BASE_PAGE_URL + suffix)
}

@Step("Сравнить фактический и ожидаемый адреса страницы")
fun assertPagePath(webDriver: WebDriver, suffix: String = "") {
    WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.urlToBe(BASE_PAGE_URL + suffix))
}

@Step("Получить вебдрайвер и открыть страницу для теста в зависимости от желаемого типа браузера")
fun getWebDriverAndOpenPage(driverType: DriverType, suffix: String = ""): WebDriver {
    val webDriver = getWebDriverForTest(driverType = driverType)
    openPageForTest(webDriver = webDriver, suffix = suffix)
    assertPagePath(webDriver = webDriver, suffix = suffix)
    return webDriver
}

@Step("Авторизоваться под пользователем")
fun authorizeUser(
    driverType: DriverType,
    email: String = "",
    password: String
): WebDriver {
    val webDriver = getWebDriverAndOpenPage(driverType = driverType, suffix = LOGIN_PAGE_PATH)
    SignInPage(webDriver = webDriver).fillSignInFieldsAndGetHomePage(email = email, password = password)
    HomePage(webDriver = webDriver)
    return webDriver
}
