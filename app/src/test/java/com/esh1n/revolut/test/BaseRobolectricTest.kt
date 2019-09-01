package com.esh1n.revolut.test

import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import test.revolut.com.revoluttest.BuildConfig

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(
    packageName = "com.esh1n.revolut.test",
    constants = BuildConfig::class,
    sdk = [21]
)
@RunWith(TestRunner::class)
open class BaseRobolectricTest