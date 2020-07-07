package com.dharyiswara.tiket.test.model

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val userList: List<User>
)