package com.applications.whazzup.flowmortarapplication.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context : Context) {
    @Provides
    internal fun provideContext(): Context {
        return context
    }
}