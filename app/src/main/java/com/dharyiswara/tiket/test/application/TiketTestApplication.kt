package com.dharyiswara.tiket.test.application

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.dharyiswara.tiket.test.di.commonModule
import com.dharyiswara.tiket.test.di.networkModule
import com.dharyiswara.tiket.test.di.repositoryModule
import com.dharyiswara.tiket.test.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TiketTestApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)

        startKoin {
            androidContext(this@TiketTestApplication)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule,
                commonModule
            )
        }
    }

}