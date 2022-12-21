package com.thoughtctl.di

import com.thoughtctl.Constants
import com.thoughtctl.api.NetworkInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val okHttp = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkInterface(retrofit: Retrofit): NetworkInterface {
        return retrofit.create(NetworkInterface::class.java)
    }

}