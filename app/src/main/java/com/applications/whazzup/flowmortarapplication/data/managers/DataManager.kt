package com.applications.whazzup.flowmortarapplication.data.managers

import com.applications.whazzup.flowmortarapplication.App
import com.applications.whazzup.flowmortarapplication.data.network.RestService
import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import com.applications.whazzup.flowmortarapplication.di.components.DaggerDataManagerComponent
import com.applications.whazzup.flowmortarapplication.di.modules.NetworkModule
import io.reactivex.Observable
import javax.inject.Inject


class DataManager {

    @Inject
    lateinit var restService : RestService

    init {
        DaggerDataManagerComponent.builder().appComponent(App.appComponent).networkModule(NetworkModule()).build().inject(this)
    }

    fun getUserFromServerAndSaveInBD(): Observable<List<UserRes>?>? {
        return restService.getUserFromNetwork().flatMap { Observable.just(it.body()) }
    }

}