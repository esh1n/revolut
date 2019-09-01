package com.esh1n.revolut.application.di

import dagger.Component
import com.esh1n.revolut.App
import com.esh1n.revolut.module.currencies.di.GetCurrenciesComponent
import com.esh1n.revolut.module.currencies.di.GetCurrenciesModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun inject(app: App)

    fun plus(getFlightsModule: GetCurrenciesModule): GetCurrenciesComponent

}