package com.dharyiswara.tiket.test.repository

import androidx.lifecycle.LiveData
import com.dharyiswara.tiket.test.base.BaseRepository
import com.dharyiswara.tiket.test.helper.AbsentLiveData
import com.dharyiswara.tiket.test.helper.ApiResponse
import com.dharyiswara.tiket.test.helper.AppExecutors
import com.dharyiswara.tiket.test.helper.CommonConstants.LIMIT
import com.dharyiswara.tiket.test.helper.Resource
import com.dharyiswara.tiket.test.model.UserResult
import com.dharyiswara.tiket.test.network.NetworkService

class UserRepository(
    private val service: NetworkService,
    private val appExecutors: AppExecutors
) {

    fun getUsers(query: String, page: Int, limit: Int = LIMIT): LiveData<Resource<UserResult>> {
        return object : BaseRepository<UserResult>(appExecutors) {
            override fun saveFromNetwork(item: UserResult) {}

            override fun shouldFetchFromNetwork(data: UserResult?): Boolean = true

            override fun loadFromLocal(): LiveData<UserResult> = AbsentLiveData.create()

            override fun loadFromNetwork(): LiveData<ApiResponse<UserResult>> =
                service.getUsers(query, page, limit)

        }.asLiveData()
    }

}