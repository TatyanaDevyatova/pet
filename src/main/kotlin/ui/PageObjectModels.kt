package ui

import io.qameta.allure.Step
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert
import utils.*

class HomePage(private val webDriver: WebDriver) {
    private val logoLink = By.xpath(".//nav//a[@class='navbar-brand']")
    private val homeLink = By.xpath(".//nav//a[text()=' Home ']")
    private val signInLink = By.xpath(".//nav//a[@href='#/$LOGIN_PAGE_PATH']")
    private val signUpLink = By.xpath(".//nav//a[@href='#/$REGISTER_PAGE_PATH']")
    private val newArticleLink = By.xpath(".//nav//a[@href='#/$EDITOR_PAGE_PATH']")
    private val settingsLink = By.xpath(".//nav//a[@href='#/$SETTINGS_PAGE_PATH']")
    private val profileLink = By.xpath(".//nav//a[contains(@href,'#/$PROFILE_PAGE_PATH')]")
    private val banner = By.xpath(".//div[@class='banner']")
    private val yourFeedTab = By.xpath(".//div[@class='container page']//a[text()=' Your Feed ']")
    private val globalFeedTab = By.xpath(".//div[@class='container page']//a[text()=' Global Feed ']")
    private val jsTab = By.xpath(".//div[@class='container page']//a[text()=' javascript ']")
    private val articleSnippetTitle = By.xpath(".//div[@class='container page']//a[@class='preview-link'][1]//h1")
    private val jsTag = By.xpath(".//div[@class='sidebar']//a[text()=' javascript ']")
    private val footer = By.xpath(".//footer")

    @Step("Проверить наличие основных элементов домашней страницы с авторизацией")
    fun checkCommonHomePageElementsWasDisplayedWithAuthorization() = setOf(
        logoLink, homeLink, newArticleLink, settingsLink, profileLink, yourFeedTab, globalFeedTab, jsTag, footer
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Проверить наличие основных элементов домашней страницы без авторизации")
    fun checkCommonHomePageElementsWasDisplayedWithoutAuthorization() = setOf(
        logoLink, homeLink, signInLink, signUpLink, banner, yourFeedTab, globalFeedTab, jsTag, footer
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Кликнуть по ссылке авторизации")
    fun clickSignInLink(): SignInPage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(signInLink)).click()
        return SignInPage(webDriver = webDriver)
    }

    @Step("Кликнуть по ссылке регистрации")
    fun clickSignUpLink(): SignUpPage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(signUpLink)).click()
        return SignUpPage(webDriver = webDriver)
    }

    @Step("Кликнуть по ссылке редактора")
    fun clickNewArticleLink(): EditorPage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(newArticleLink)).click()
        return EditorPage(webDriver = webDriver)
    }

    @Step("Получить имя пользователя")
    fun getProfileLink(): String? {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(profileLink))
        return webDriver.findElement(profileLink).getAttribute("href")
    }

    @Step("Кликнуть по тэгу 'javascript'")
    fun clickJsTag() {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(jsTag)).click()
    }

    @Step("Проверить наличие вкладки 'javascript'")
    fun checkJsTabIsDisplayed(): Boolean {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(jsTab))
        return webDriver.findElement(jsTab).isDisplayed
    }

    @Step("Получить заголовок сниппета статьи")
    fun getArticleSnippetTitle(): String {
        WebDriverWait(webDriver, COMMON_TIMEOUT)
            .until(ExpectedConditions.visibilityOfElementLocated(articleSnippetTitle))
        return webDriver.findElement(articleSnippetTitle).text
    }

    @Step("Кликнуть по заголовку сниппета статьи")
    fun clickArticleSnippet(): ArticlePage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(articleSnippetTitle))
            .click()
        return ArticlePage(webDriver = webDriver)
    }
}

class SignInPage(private val webDriver: WebDriver) {
    private val title = By.xpath(".//h1")
    private val registerLink = By.xpath(".//a[@href='#/$REGISTER_PAGE_PATH']")
    private val errorMessage = By.xpath(".//ul[@class='error-messages']")
    private val emailInput = By.xpath(".//input[@placeholder='Email']")
    private val passwordInput = By.xpath(".//input[@placeholder='Password']")
    private val signInButton = By.xpath(".//button[text()=' Sign in ']")

    @Step("Проверить наличие основных элементов страницы авторизации")
    fun checkCommonSignInPageElementsWasDisplayed() = setOf(
        title, registerLink, emailInput, passwordInput, signInButton
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Проверить кликабельность кнопки авторизации")
    fun checkSignInButtonIsClickable(): Boolean {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(signInButton))
        return webDriver.findElement(signInButton).isEnabled
    }

    @Step("Ввести электронную почту")
    fun sendEmail(email: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(emailInput))
            .sendKeys(email)
    }

    @Step("Ввести пароль")
    fun sendPassword(password: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(passwordInput))
            .sendKeys(password)
    }

    @Step("Нажать кнопку авторизации")
    fun clickSignInButton() {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(signInButton)).click()
    }

    @Step("Дождаться редиректа на домашнюю страницу")
    private fun waitRedirectToHomePage(): HomePage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(signInButton))
        assertPagePath(webDriver = webDriver)
        return HomePage(webDriver = webDriver)
    }

    @Step("Заполнить поля страницы авторизации и нажать кнопку авторизации")
    fun fillSignInFieldsAndGetHomePage(email: String, password: String): HomePage {
        sendEmail(email)
        sendPassword(password)
        Assert.assertTrue(checkSignInButtonIsClickable())
        clickSignInButton()
        return waitRedirectToHomePage()
    }

    @Step("Кликнуть по ссылке регистрации")
    fun clickRegisterLink(): SignUpPage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(registerLink)).click()
        return SignUpPage(webDriver = webDriver)
    }

    @Step("Проверить наличие сообщения об ошибке")
    fun checkErrorWasDisplayed(): Boolean {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
        return webDriver.findElement(errorMessage).isDisplayed
    }
}

class SignUpPage(private val webDriver: WebDriver) {
    private val title = By.xpath(".//h1")
    private val loginLink = By.xpath(".//a[@href='#/$LOGIN_PAGE_PATH']")
    private val usernameInput = By.xpath(".//input[@placeholder='Username']")
    private val emailInput = By.xpath(".//input[@placeholder='Email']")
    private val passwordInput = By.xpath(".//input[@placeholder='Password']")
    private val signUpButton = By.xpath(".//button[text()=' Sign up ']")

    @Step("Проверить наличие основных элементов страницы регистрации")
    fun checkCommonSignUpPageElementsWasDisplayed() = setOf(
        title, loginLink, usernameInput, emailInput, passwordInput, signUpButton
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Проверить кликабельность кнопки регистрации")
    fun checkSignUpButtonIsClickable(): Boolean {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(signUpButton))
        return webDriver.findElement(signUpButton).isEnabled
    }

    @Step("Ввести имя пользователя")
    fun sendUsername(username: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(usernameInput))
            .sendKeys(username)
    }

    @Step("Ввести электронную почту")
    fun sendEmail(email: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(emailInput))
            .sendKeys(email)
    }

    @Step("Ввести пароль")
    fun sendPassword(password: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(passwordInput))
            .sendKeys(password)
    }

    @Step("Нажать кнопку регистрации")
    private fun clickSignUpButton(): HomePage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(signUpButton)).click()
        assertPagePath(webDriver = webDriver, suffix = REGISTER_PAGE_PATH)
        return HomePage(webDriver = webDriver)
    }

    @Step("Заполнить поля страницы регистрации и нажать кнопку регистрации")
    fun fillSignUpFieldsAndGetHomePage(username: String, email: String, password: String): HomePage {
        sendUsername(username)
        sendEmail(email)
        sendPassword(password)
        Assert.assertTrue(checkSignUpButtonIsClickable())
        return clickSignUpButton()
    }

    @Step("Кликнуть по ссылке авторизации")
    fun clickLoginLink(): SignInPage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(loginLink)).click()
        return SignInPage(webDriver = webDriver)
    }
}

class EditorPage(private val webDriver: WebDriver) {
    private val errorMessage = By.xpath(".//ul[@class='error-messages']")
    private val titleInput = By.xpath(".//input[@placeholder='Article Title']")
    private val descriptionInput = By.xpath(".//input[@formcontrolname='description']")
    private val bodyArea = By.xpath(".//textarea[@placeholder='Write your article (in markdown)']")
    private val tagsInput = By.xpath(".//input[@placeholder='Enter tags']")
    private val publishButton = By.xpath(".//button[text()=' Publish Article ']")

    @Step("Проверить наличие основных элементов страницы редактора")
    fun checkCommonEditorPageElementsWasDisplayed() = setOf(
        titleInput, descriptionInput, bodyArea, tagsInput, publishButton
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Ввести заголовок")
    fun sendTitle(title: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(titleInput))
            .sendKeys(title)
    }

    @Step("Ввести описание")
    fun sendDescription(description: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(descriptionInput))
            .sendKeys(description)
    }

    @Step("Ввести текст")
    fun sendBody(body: String) {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(bodyArea))
            .sendKeys(body)
    }

    @Step("Нажать кнопку публикации")
    fun clickPublishButton() {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.elementToBeClickable(publishButton)).click()
    }

    @Step("Дождаться редиректа на страницу статьи")
    private fun waitRedirectToArticlePage(title: String): ArticlePage {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(publishButton))
        assertPagePath(webDriver = webDriver, suffix = ARTICLE_PAGE_PATH + title)
        return ArticlePage(webDriver = webDriver)
    }

    @Step("Заполнить поля статьи и нажать кнопку публикации")
    fun fillEditorFieldsAndGetHomePage(articleTitle: String, description: String, body: String): ArticlePage {
        sendTitle(articleTitle)
        sendDescription(description)
        sendBody(body)
        clickPublishButton()
        return waitRedirectToArticlePage(articleTitle)
    }

    @Step("Проверить наличие сообщения об ошибке")
    fun checkErrorWasDisplayed(): Boolean {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
        return webDriver.findElement(errorMessage).isDisplayed
    }
}

class ArticlePage(private val webDriver: WebDriver) {
    private val title = By.xpath(".//div[@class='banner']//h1")
    private val body = By.xpath(".//div[@class='container page']//p")
    private val tags = By.xpath(".//ul[@class='tag-list']")
    private val authorLink = By.xpath(".//div[@class='article-actions']//a")

    @Step("Проверить наличие основных элементов страницы статьи")
    fun checkCommonArticlePageElementsWasDisplayed() = setOf(
        title, body, tags, authorLink
    ).forEach {
        Assert.assertNotNull(
            WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(it))
        )
    }

    @Step("Получить заголовок статьи")
    fun getTitle(): String? {
        WebDriverWait(webDriver, COMMON_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(title))
        return webDriver.findElement(title).text
    }
}
