package utils

import api.ArticleDto
import java.time.Duration

// region [UI]
val COMMON_TIMEOUT: Duration = Duration.ofSeconds(5)
const val BASE_PAGE_URL = "https://demo.realworld.show/#/"
const val LOGIN_PAGE_PATH = "login"
const val REGISTER_PAGE_PATH = "register"
const val EDITOR_PAGE_PATH = "editor"
const val SETTINGS_PAGE_PATH = "settings"
const val PROFILE_PAGE_PATH = "profile/"
const val ARTICLE_PAGE_PATH = "article/"
//endregion

// region [API]
const val BASE_URL = "https://api.realworld.show"
const val POST_USERS_PATH = "/api/users"
const val POST_LOGIN_PATH = "/api/users/login"
const val GET_TAGS_PATH = "/api/tags"
const val GET_ARTICLES_PATH = "/api/articles"
const val GET_ARTICLE_BY_SLUG = "/api/articles/{slug}"

const val OK_200 = "HTTP/1.1 200 OK"
const val CREATED_201 = "HTTP/1.1 201 Created"
const val ERROR_401 = "HTTP/1.1 401 Error"
const val NOT_FOUND_404 = "HTTP/1.1 404 Not Found"
const val ERROR_422 = "HTTP/1.1 422 Error"
const val EXPECTED_IMAGE =
    "https://raw.githubusercontent.com/gothinkster/node-express-realworld-example-app/refs/heads/master/src/assets/images/smiley-cyrus.jpeg"
const val EXPECTED_SLUG = "how-to-learn-javascript-efficiently"
const val EXPECTED_TITLE = "How to Learn JavaScript Efficiently"
const val EXPECTED_DESCRIPTION = "A comprehensive guide to mastering JavaScript from beginner to advanced level"
val EXPECTED_BODY =
    """Learning JavaScript can be overwhelming with so many resources available. Here's a structured approach that has helped thousands of developers master this essential language.

## Start with the Fundamentals

Before diving into frameworks, master the core concepts: variables, functions, objects, and arrays. Understanding these building blocks is crucial for writing clean, maintainable code.

## Practice with Real Projects

The best way to learn is by building actual applications. Start with simple projects like a todo list or calculator, then gradually increase complexity.

## Join the Community

Engage with other developers through forums, Discord servers, and local meetups. The JavaScript community is incredibly welcoming and helpful.""".trimMargin()
val EXPECTED_TAG_LIST = listOf("beginners", "javascript", "programming", "webdev")
val EXPECTED_AUTHOR = ArticleDto.Author(
    username = "johndoe",
    bio = "Full-stack developer passionate about clean code and innovative solutions. Love working with modern web technologies.",
    image = EXPECTED_IMAGE,
    following = false
)
//endregion
