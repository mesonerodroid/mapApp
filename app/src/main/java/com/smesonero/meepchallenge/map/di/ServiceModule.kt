package com.smesonero.meepchallenge.map.di

import com.google.gson.GsonBuilder
import com.smesonero.meepchallenge.base.EitherCallAdapterFactory
import com.smesonero.meepchallenge.ws.service.ResourceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideMapService(): ResourceService {
        val webservice by lazy {
            Retrofit.Builder()
                .baseUrl(ResourceService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(EitherCallAdapterFactory())
                .build().create(ResourceService::class.java)

        }
        return webservice
    }
}