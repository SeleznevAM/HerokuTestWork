package com.applications.whazzup.flowmortarapplication.mvp.models

import com.applications.whazzup.flowmortarapplication.data.storage.dto.UserDto
import com.applications.whazzup.flowmortarapplication.data.storage.realm.UserRealm
import io.reactivex.Observable


class UserListModel : AbstractModel() {
    fun getUserFromRealm() : Observable<UserRealm> {
       return realmManager.getUserFromRealm()
    }
}