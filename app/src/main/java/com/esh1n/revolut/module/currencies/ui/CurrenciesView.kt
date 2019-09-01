package com.esh1n.revolut.module.currencies.ui


import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.adapter.SimpleRecyclerAdapter
import com.avito.konveyor.blueprint.ViewHolderBuilder
import test.revolut.com.revoluttest.R

interface CurrenciesView {

    fun onDataSourceChanged()

    fun onDataRangeChanged(start: Int, end: Int)
}

class CurrenciesViewImpl(
        view: ViewGroup,
        private val viewHolderBuilder: ViewHolderBuilder<BaseViewHolder>,
        private val adapterPresenter: AdapterPresenter
) : CurrenciesView {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
    private lateinit var adapter : SimpleRecyclerAdapter

    init {
        recyclerView.layoutManager = layoutManager
    }

    override fun onDataSourceChanged() {
        if (recyclerView.adapter == null) {
            adapter = SimpleRecyclerAdapter(adapterPresenter, viewHolderBuilder)
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        } else {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDataRangeChanged(start: Int, end: Int) {
        recyclerView.adapter!!.notifyItemRangeChanged(start, end)
    }
}