package com.esh1n.revolut.application.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import com.esh1n.revolut.App
import com.esh1n.revolut.module.CurrencyRatesRepositoryImpl
import com.esh1n.revolut.utils.SchedulersFactory
import com.esh1n.revolut.utils.SchedulersFactoryImpl
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class ApplicationModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideAvitoApp(): App {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app.applicationContext
    }


    @Provides
    @Singleton
    fun provideSchedulersFactory(): SchedulersFactory {
        return SchedulersFactoryImpl()
    }

    @Provides
    @Singleton
    internal fun provideCurrencyRatesRepository(): CurrencyRatesRepositoryImpl {
        return CurrencyRatesRepositoryImpl()
    }

}