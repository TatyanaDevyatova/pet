package api

import io.qameta.allure.Step
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import utils.*

class Client {
    private fun getSpecification(): RequestSpecification {
        return RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_URL)
            .build()
    }

    @Step(value = "Зарегистрировать пользователя")
    fun postUsers(postUsersBody: PostUsersBody): ValidatableResponse {
        return RestAssured.given()
            .spec(getSpecification())
            .body(postUsersBody)
            .`when`()
            .post(POST_USERS_PATH)
            .then()
    }

    @Step(value = "Авторизовать пользователя")
    fun postLogin(postLoginBody: PostLoginBody): ValidatableResponse {
        return RestAssured.given()
            .spec(getSpecification())
            .body(postLoginBody)
            .`when`()
            .post(POST_LOGIN_PATH)
            .then()
    }

    @Step(value = "Получить список тэгов")
    fun getTags(): ValidatableResponse {
        return RestAssured.given()
            .spec(getSpecification())
            .`when`()
            .get(GET_TAGS_PATH)
            .then()
    }

    @Step(value = "Получить список статей")
    fun getArticles(limit: Int, offset: Int): ValidatableResponse {
        return RestAssured.given()
            .spec(getSpecification())
            .queryParam("limit", limit)
            .queryParam("offset", offset)
            .`when`()
            .get(GET_ARTICLES_PATH)
            .then()
    }

    @Step(value = "Получить статью")
    fun getArticleBySlug(slug: String): ValidatableResponse {
        return RestAssured.given()
            .spec(getSpecification())
            .basePath(GET_ARTICLE_BY_SLUG)
            .pathParam("slug", slug)
            .`when`()
            .get()
            .then()
    }
}
