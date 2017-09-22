package com.applications.whazzup.flowmortarapplication.di.components

import com.applications.whazzup.flowmortarapplication.data.managers.DataManager
import com.applications.whazzup.flowmortarapplication.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton


@Component(dependencies = arrayOf(AppComponent :: class), modules = arrayOf(NetworkModule :: class))
@Singleton
interface DataManagerComponent {
    fun inject(manager: DataManager)
}