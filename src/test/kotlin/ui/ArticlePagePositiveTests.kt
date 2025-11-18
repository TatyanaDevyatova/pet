package ui

import io.qameta.allure.Epic
import io.qameta.allure.Feature
import org.openqa.selenium.WebDriver
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import utils.ARTICLE_PAGE_PATH
import utils.EXPECTED_SLUG
import utils.Epics
import utils.Features

@Epic(Epics.UI)
@Feature(Features.ARTICLE_PAGE)
class ArticlePagePositiveTests {

    private lateinit var driver: WebDriver

    @AfterMethod
    fun cleanUp() {
        driver.quit()
    }

    @Test(
        description = "На странице статьи должны быть отображены все основные элементы",
        dataProvider = "driverData", dataProviderClass = DriverDataProviders::class
    )
    fun shouldBeDisplayedAllCommonArticlePageElements(driverType: DriverType) {
        driver = getWebDriverAndOpenPage(driverType = driverType)
        HomePage(webDriver = driver).clickArticleSnippet()
        assertPagePath(webDriver = driver, suffix = ARTICLE_PAGE_PATH + EXPECTED_SLUG)
        ArticlePage(webDriver = driver).checkCommonArticlePageElementsWasDisplayed()
    }
}
