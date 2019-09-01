package com.esh1n.revolut.module.currencies.item
import com.avito.konveyor.blueprint.Item

interface Item : Item {

    val stringId: String

    override val id: Long
        get() = stringId.hashCode().toLong()
}