package com.thoughtctl.di

import com.thoughtctl.Constants
import com.thoughtctl.api.NetworkInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().
                baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkInterface(retrofit:Retrofit):NetworkInterface {
        return retrofit.create(NetworkInterface::class.java)
    }

}