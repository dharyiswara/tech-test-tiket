package com.dharyiswara.tiket.test.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dharyiswara.tiket.test.helper.Resource
import com.dharyiswara.tiket.test.model.UserResult
import com.dharyiswara.tiket.test.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val requestGetUser = MutableLiveData<UserRequest>()

    val userListLiveData: LiveData<Resource<UserResult>> = Transformations
        .switchMap(requestGetUser) {
            repository.getUsers(it.query, it.page)
        }

    fun getUser(query: String, page: Int) {
        val request = UserRequest(query, page)
        requestGetUser.value = request
    }

    private data class UserRequest(
        val query: String,
        val page: Int
    )

}