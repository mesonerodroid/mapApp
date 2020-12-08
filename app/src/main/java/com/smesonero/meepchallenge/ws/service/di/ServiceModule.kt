package com.smesonero.meepchallenge.ws.service.di

import com.google.gson.GsonBuilder
import com.smesonero.meepchallenge.base.EitherCallAdapterFactory
import com.smesonero.meepchallenge.ws.service.ResourceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.w3c.dom.ls.LSResourceResolver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    @Named("ResourceService")
    fun provideProductService(): ResourceService {
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