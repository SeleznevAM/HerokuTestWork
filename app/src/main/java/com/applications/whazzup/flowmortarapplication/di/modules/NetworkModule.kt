package com.applications.whazzup.flowmortarapplication.di.modules

import com.applications.whazzup.flowmortarapplication.data.network.RestService
import com.applications.whazzup.flowmortarapplication.util.AppConfig
import com.applications.whazzup.flowmortarapplication.util.ConstantManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return createClient()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return createRetrofit(okHttpClient)
    }

    @Provides
    @Singleton
    internal fun provideRestService(retrofit: Retrofit): RestService {
        return retrofit.create(RestService::class.java)
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(ConstantManager.MAX_CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(ConstantManager.MAX_READ_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(ConstantManager.MAX_WRITE_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build()
    }

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(createConvertFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    private fun createConvertFactory(): Converter.Factory {
        return MoshiConverterFactory.create()
    }
}