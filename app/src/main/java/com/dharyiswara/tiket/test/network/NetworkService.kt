package com.dharyiswara.tiket.test.network

import androidx.lifecycle.LiveData
import com.dharyiswara.tiket.test.helper.ApiResponse
import com.dharyiswara.tiket.test.model.UserResult
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("search/users")
    fun getUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): LiveData<ApiResponse<UserResult>>

}