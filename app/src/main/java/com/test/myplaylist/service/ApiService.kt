package com.test.myplaylist.service

import com.test.myplaylist.domain.model.UserListResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService{

    @GET("api/users")
   suspend fun getUserList(@QueryMap queryMap: MutableMap<String, String>): UserListResponse
}