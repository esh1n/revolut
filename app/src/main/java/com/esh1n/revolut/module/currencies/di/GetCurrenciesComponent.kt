package com.esh1n.revolut.module.currencies.di

import dagger.Subcomponent
import com.esh1n.revolut.module.currencies.ui.CurrenciesActivity

@Subcomponent(modules = [GetCurrenciesModule::class])
@PerActivity
interface GetCurrenciesComponent {

    fun inject(activity: CurrenciesActivity)
}