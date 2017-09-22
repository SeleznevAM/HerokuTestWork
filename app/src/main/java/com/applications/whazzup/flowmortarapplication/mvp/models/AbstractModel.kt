package com.applications.whazzup.flowmortarapplication.mvp.models

import com.applications.whazzup.flowmortarapplication.data.managers.DataManager
import com.applications.whazzup.flowmortarapplication.data.managers.RealmManager
import com.applications.whazzup.flowmortarapplication.di.components.DaggerDataManagerComponent
import com.applications.whazzup.flowmortarapplication.di.components.DaggerModelComponent
import com.applications.whazzup.flowmortarapplication.di.modules.ModelModule
import javax.inject.Inject


abstract class AbstractModel {
    @Inject
    lateinit var dataManager : DataManager

    @Inject
    lateinit var realmManager : RealmManager

    init{
        DaggerModelComponent.builder().modelModule(ModelModule()).build().inject(this)
    }

}