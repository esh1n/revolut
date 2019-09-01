package com.esh1n.revolut

import android.app.Application
import com.esh1n.revolut.application.di.ApplicationComponent
import com.esh1n.revolut.application.di.ApplicationModule
import com.esh1n.revolut.application.di.DaggerApplicationComponent

class App : Application() {

    lateinit var component: ApplicationComponent

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component.inject(this)
    }

}