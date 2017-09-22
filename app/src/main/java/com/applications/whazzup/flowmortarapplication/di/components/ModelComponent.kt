package com.applications.whazzup.flowmortarapplication.di.components

import com.applications.whazzup.flowmortarapplication.di.modules.ModelModule
import com.applications.whazzup.flowmortarapplication.mvp.models.AbstractModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ModelModule :: class))
@Singleton
interface ModelComponent {
    fun inject(model : AbstractModel)
}