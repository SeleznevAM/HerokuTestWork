package com.applications.whazzup.flowmortarapplication.di.modules

import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.mvp.models.RootModel
import com.applications.whazzup.flowmortarapplication.mvp.presenters.RootPresenter
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import dagger.Module
import dagger.Provides

/**
 * Created by Alex on 21.09.2017.
 */
@Module
class RootModule {
    @Provides
    @DaggerScope(RootActivity::class)
    internal fun provideRootPresenter(): RootPresenter {
        return RootPresenter.INSTANCE
    }

    @Provides
    @DaggerScope(RootActivity::class)
    internal fun provideRootModel(): RootModel {
        return RootModel()
    }
}