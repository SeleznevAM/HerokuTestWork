package com.applications.whazzup.flowmortarapplication.mvp.models

import io.reactivex.Completable
import io.reactivex.Observable


class SplashModel : AbstractModel() {
    fun getUserFromNetworkAndSaveInBd(): Completable? {
        return dataManager.getUserFromServerAndSaveInBD()?.flatMap { Observable.fromIterable(it) }?.doOnNext { realmManager.saveUserToRealm(it) }?.ignoreElements()
    }
}