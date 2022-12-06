package com.test.myplaylist.domain.repository

import com.test.myplaylist.domain.model.UsersList
import com.test.myplaylist.service.ApiService

class RepositoryImpl constructor(
    private val apiService: ApiService,
) : Repository {

    override suspend fun userList(queryMap: MutableMap<String, String>): UsersList =
        apiService.getUserList(queryMap).userList()
}