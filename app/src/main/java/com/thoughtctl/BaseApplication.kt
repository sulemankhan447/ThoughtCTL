package com.thoughtctl

import android.app.Application
import android.content.Context
import com.thoughtctl.di.AppComponent
import com.thoughtctl.di.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent
    companion object{
         var context:Context?=null
    }
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        context = this
    }
}