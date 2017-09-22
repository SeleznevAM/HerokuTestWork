package com.applications.whazzup.flowmortarapplication.data.managers

import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import com.applications.whazzup.flowmortarapplication.data.storage.realm.UserRealm
import com.milkmachine.rxjava2interop.toV2Observable
import io.reactivex.Observable
import io.realm.Realm


open class RealmManager {

    fun saveUserToRealm(user: UserRes) {
        val realm = Realm.getDefaultInstance()
        var userRealm = UserRealm(user)
        realm.executeTransaction { realm -> realm.insertOrUpdate(userRealm) }
        realm.close()
    }

    fun getUserFromRealm() : Observable<UserRealm> {
        val realm = Realm.getDefaultInstance()
        var managerUser = realm.where(UserRealm :: class.java).findAllAsync()
        return managerUser.asObservable().toV2Observable().filter { it.isLoaded }.firstElement().flatMapObservable{Observable.fromIterable(it)}
    }

}