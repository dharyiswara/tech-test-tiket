package com.dharyiswara.tiket.test.di

import com.dharyiswara.tiket.test.helper.AppExecutors
import com.dharyiswara.tiket.test.network.NetworkFactory
import com.dharyiswara.tiket.test.repository.UserRepository
import com.dharyiswara.tiket.test.ui.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {

    single { NetworkFactory.makeNetworkService(get(), get(), get()) }

    single { NetworkFactory.makeClientService(get(), get()) }

    single { NetworkFactory.makeLoggingInterceptor() }

    single { NetworkFactory.makeGson() }

    single { NetworkFactory.makeCache(get()) }
}

val repositoryModule = module {

    single { UserRepository(get(), get()) }

}

val viewModelModule = module {

    viewModel { MainViewModel(get()) }

}

val commonModule = module {

    single { AppExecutors() }

}