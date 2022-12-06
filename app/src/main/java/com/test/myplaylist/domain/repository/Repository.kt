package com.test.myplaylist.domain.repository

import com.test.myplaylist.domain.model.UsersList

interface Repository {
    suspend fun userList(queryMap: MutableMap<String, String>): UsersList
}