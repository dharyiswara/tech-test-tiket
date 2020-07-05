package com.dharyiswara.tiket.test.network

import android.content.Context
import com.dharyiswara.tiket.test.BuildConfig
import com.dharyiswara.tiket.test.R
import com.dharyiswara.tiket.test.helper.LiveDataCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkFactory {

    fun makeNetworkService(
        context: Context,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): NetworkService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(context.getString(R.string.server_url))
            .build()
        return retrofit.create(NetworkService::class.java)
    }

    fun makeClientService(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader("Content-Type", "application/json")
                chain.proceed(ongoing.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
    }

    fun makeGson(): Gson {
        return GsonBuilder()
            .create()
    }

    fun makeCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

}