package com.test.myplaylist.di.usecase

import com.test.myplaylist.domain.model.UserListResponse
import com.test.myplaylist.domain.model.UsersList
import com.test.myplaylist.domain.repository.Repository
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class UserCase @Inject constructor(private val repository: Repository) {

    suspend fun executeUserList(): UseCaseResult<UsersList> {
        val queries: MutableMap<String, String> = mutableMapOf()
        queries["page"] = "1"
        return try {
            val response = repository.userList(queries)
            UseCaseResult.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is UnknownHostException -> UseCaseResult.NetworkError
                is ConnectException -> UseCaseResult.NetworkError
                else -> UseCaseResult.Error(e)
            }
        }

    }
}