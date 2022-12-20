package com.thoughtctl.di

import com.thoughtctl.SearchActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(searchActivity: SearchActivity)
}