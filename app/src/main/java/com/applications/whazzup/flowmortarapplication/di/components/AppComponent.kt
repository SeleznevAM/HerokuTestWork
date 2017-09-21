package com.applications.whazzup.flowmortarapplication.di.components

import android.content.Context
import com.applications.whazzup.flowmortarapplication.di.modules.AppModule
import dagger.Component


@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    val context: Context
}