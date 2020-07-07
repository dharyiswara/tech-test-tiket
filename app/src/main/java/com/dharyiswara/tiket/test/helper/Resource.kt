package com.dharyiswara.tiket.test.helper

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val throwable: Throwable? = null
) {
    companion object {

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data)
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> limit(): Resource<T> {
            return Resource(Status.LIMIT, null, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null, null)
        }

        fun <T> networkError(throwable: Throwable? = null): Resource<T> {
            return Resource(Status.NETWORK_ERROR, null, throwable)
        }

        fun <T> error(throwable: Throwable? = null): Resource<T> {
            return Resource(Status.ERROR, null, throwable)
        }
    }
}