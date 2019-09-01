package com.esh1n.revolut.module.currencies

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import com.esh1n.revolut.application.api.RevolutApi
import com.esh1n.revolut.module.CurrencyRatesUpdater
import com.esh1n.revolut.utils.SchedulersFactory
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

interface GetCurrenciesInteractor {

    fun startPolling(): Flowable<Unit>

}

class GetCurrenciesInteractorImpl(private val api: RevolutApi,
                                  private val schedulersFactory: SchedulersFactory,
                                  private val currencyRatesUpdater: CurrencyRatesUpdater) : GetCurrenciesInteractor {

    override fun startPolling(): Flowable<Unit> {
        return Observable.interval(1, TimeUnit.SECONDS)
                .flatMap { api.get().subscribeOn(schedulersFactory.io()) }
                .toFlowable(BackpressureStrategy.DROP)
                .doOnNext {
                    val rates = it.rates.toMutableMap()
                    rates[it.baseCurrency] = BigDecimal(1)
                    currencyRatesUpdater.updateRates(it.baseCurrency, rates)
                }
                .subscribeOn(schedulersFactory.io())
                .map { }
    }

}