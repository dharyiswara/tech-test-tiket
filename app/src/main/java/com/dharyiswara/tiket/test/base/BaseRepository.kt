package com.dharyiswara.tiket.test.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dharyiswara.tiket.test.helper.*

abstract class BaseRepository<Type>(
    private val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Resource<Type>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromLocal()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetchFromNetwork(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<Type>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<Type>) {
        val apiResponse = loadFromNetwork()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val newResponse = processResponse(response)
                        saveFromNetwork(newResponse)
                        appExecutors.mainThread().execute {
                            // reload immediately from network
                            setValue(Resource.success(newResponse))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        setValue(Resource.empty())
                    }
                }
                is ApiLimitResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.limit())
                    }
                }
                is ApiErrorResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        setValue(Resource.error(response.error))
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<Type>>

    @WorkerThread
    protected open fun processResponse(
        response: ApiSuccessResponse<Type>
    ) = response.body

    @WorkerThread
    protected abstract fun saveFromNetwork(item: Type)

    @MainThread
    protected abstract fun shouldFetchFromNetwork(data: Type?): Boolean

    @MainThread
    protected abstract fun loadFromLocal(): LiveData<Type>

    @MainThread
    protected abstract fun loadFromNetwork(): LiveData<ApiResponse<Type>>
}