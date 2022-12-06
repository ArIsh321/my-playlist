package com.test.myplaylist.domain.model

import com.squareup.moshi.Json

data class UserListResponse (
    @Json(name = "page") var page : Int = 0,
    @Json(name = "per_page")  var per_page : Int = 0,
    @Json(name = "total")  var total : Int = 0,
    @Json(name = "total_pages")  var total_pages : Int = 0,
    @Json(name = "data")  var data : List<UserDetails>?
    )

{
    data class UserDetails(
        @Json(name = "id") val id: Int = -1,
        @Json(name = "email") val email: String = "",
        @Json(name = "first_name") val firstName: String ="",
        @Json(name = "last_name") val lastName: String = "",
        @Json(name = "avatar") val avatar: String ="",
    )
    {
        fun toUserList()  : Users{
            return Users(
                id = this.id,
                email = this.email,
                firstName = this.firstName,
                lastName = this.lastName,
                avatar = this.avatar,
            )
        }
    }

    private fun toUsersList(): List<Users> {
        return this.data!!.map { it.toUserList()}
    }

    fun userList(): UsersList {
        return UsersList(
            users = this.toUsersList(),
        )
    }
}
