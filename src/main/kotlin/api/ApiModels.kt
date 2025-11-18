package api

data class UserDto(
    val username: String?,
    val email: String?,
    val bio: String?,
    val image: String?,
    val token: String?,
)

data class ArticleDto(
    val slug: String?,
    val title: String?,
    val description: String?,
    val body: String?,
    val tagList: List<String?>?,
    val createdAt: String?,
    val updatedAt: String?,
    val favorited: Boolean,
    val favoritesCount: Int?,
    val author: Author
) {
    data class Author(
        val username: String?,
        val bio: String?,
        val image: String?,
        val following: Boolean?
    )
}

data class PostUsersBody(
    val user: PostUsers,
) {
    data class PostUsers(
        val email: String? = null,
        val password: String? = null,
        val username: String? = null,
    )
}

data class PostLoginBody(
    val user: PostLogin,
) {
    data class PostLogin(
        val email: String? = null,
        val password: String? = null,
    )
}
