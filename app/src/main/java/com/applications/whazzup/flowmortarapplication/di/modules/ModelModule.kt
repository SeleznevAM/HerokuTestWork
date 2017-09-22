package com.applications.whazzup.flowmortarapplication.di.modules

import com.applications.whazzup.flowmortarapplication.data.managers.DataManager
import com.applications.whazzup.flowmortarapplication.data.managers.RealmManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {
    @Provides
    @Singleton
    internal fun provideDataManager(): DataManager{
        return DataManager()
    }

    @Provides
    @Singleton
    internal  fun provideRealmManager() : RealmManager{
        return RealmManager()
    }


}