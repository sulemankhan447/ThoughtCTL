package com.thoughtctl

import android.app.Application
import com.thoughtctl.di.AppComponent
import com.thoughtctl.di.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}