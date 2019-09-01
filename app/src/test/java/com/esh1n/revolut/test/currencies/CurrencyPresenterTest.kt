package com.esh1n.revolut.test.currencies

import android.os.Bundle
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.data_source.DataSource
import com.avito.konveyor.data_source.ListDataSource
import com.nhaarman.mockito_kotlin.*
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import com.esh1n.revolut.module.currencies.CurrenciesPresenter
import com.esh1n.revolut.module.currencies.CurrenciesPresenterImpl
import com.esh1n.revolut.module.currencies.CurrencyValueTransformer
import com.esh1n.revolut.module.currencies.GetCurrenciesInteractor
import com.esh1n.revolut.module.currencies.item.CurrencyItem
import com.esh1n.revolut.module.currencies.ui.CurrenciesView
import com.esh1n.revolut.utils.TestSchedulersFactory
import com.esh1n.revolut.test.Is
import java.math.BigDecimal

@Suppress("IllegalIdentifier")
class CurrencyPresenterTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var adapterPresenter: AdapterPresenter
    @Mock
    private lateinit var interactor: GetCurrenciesInteractor
    @Mock
    private lateinit var view: CurrenciesView
    @Mock
    private lateinit var currencyValueTransformer: CurrencyValueTransformer

    private lateinit var presenter: CurrenciesPresenter
    private val currentValueSubject = PublishSubject.create<Pair<String, BigDecimal>>()
    private val ratesPollingObservable = PublishSubject.create<Unit>()

    @Before
    fun setUp() {
        mockInteractor()
    }

    @Test
    fun `attach view - updates view after new rates loaded `() {
        createPresenter(state = null)
        val itemsList = listOf(createRandomItem())
        mockTransformItems(itemsList)
        presenter.attachView(view)
        currentValueSubject.onNext(Pair("EUR", 10.toBigDecimal()))
        clearInvocations(adapterPresenter)

        ratesPollingObservable.onNext(Unit)

        val capturedDataSource = captureAdapterDataSource()
        assertThat(capturedDataSource, Is(itemsList))
    }

    @Test
    fun `attach view - doesnt update data source after polling result - state is null and cached source is null`() {
        createPresenter(state = null)
        val itemsList = listOf(createRandomItem())
        mockTransformItems(itemsList)
        presenter.attachView(view)
        clearInvocations(adapterPresenter)

        ratesPollingObservable.onNext(Unit)

        verify(adapterPresenter, never()).onDataSourceChanged(any())
    }


    private fun mockTransformItems(items: List<CurrencyItem>) {
        whenever(currencyValueTransformer.transformItems(any(), any())).thenReturn(items)
    }

    private fun mockInteractor() {
        whenever(interactor.startPolling()).thenReturn(ratesPollingObservable.toFlowable(BackpressureStrategy.DROP))
    }

    private fun captureAdapterDataSource(): List<CurrencyItem> {
        val captor = argumentCaptor<DataSource<CurrencyItem>>()
        verify(adapterPresenter).onDataSourceChanged(captor.capture())
        return (captor.lastValue as ListDataSource<CurrencyItem>).toList()
    }

    private fun createPresenter(state: Bundle? = null) {
        presenter = CurrenciesPresenterImpl(
                adapterPresenter = adapterPresenter,
                schedulersFactory = TestSchedulersFactory(),
                interactor = interactor,
                currencyObservable = currentValueSubject,
                currencyValueTransformer = currencyValueTransformer,
                savedState = state
        )
    }

    private fun createRandomItem(): CurrencyItem {
        return CurrencyItem(System.currentTimeMillis().toString(), System.currentTimeMillis().toBigDecimal(), false)
    }

    private fun recreatePresenter() {
        createPresenter(presenter.onSaveState())
    }

}