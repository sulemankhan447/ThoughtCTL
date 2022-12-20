package com.thoughtctl.di

import androidx.lifecycle.ViewModel
import com.thoughtctl.SearchActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class,ViewModelModule::class])
interface AppComponent {

    fun inject(searchActivity: SearchActivity)

    fun getMap(): Map<Class<*>, ViewModel>
}