package com.thoughtctl.di

import androidx.lifecycle.ViewModel
import com.thoughtctl.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @ClassKey(SearchViewModel::class)
    @IntoMap
    @Binds
    abstract fun providesSearchViewModel(searchViewModel: SearchViewModel): ViewModel
}