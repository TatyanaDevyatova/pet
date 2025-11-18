package api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.testng.Assert

val mapper = jacksonObjectMapper().registerKotlinModule()

object ApiHelper {
    fun registerUser(
        client: Client,
        postUsersBody: PostUsersBody
    ): ValidatableResponse = client.postUsers(postUsersBody).apply {
        Assert.assertEquals(extract().statusCode(), HttpStatus.SC_CREATED)
    }
}
