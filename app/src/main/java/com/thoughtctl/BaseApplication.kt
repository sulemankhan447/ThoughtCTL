package com.thoughtctl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.thoughtctl.di.AppComponent
import com.thoughtctl.di.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}