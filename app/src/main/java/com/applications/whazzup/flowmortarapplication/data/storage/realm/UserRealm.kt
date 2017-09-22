package com.applications.whazzup.flowmortarapplication.data.storage.realm

import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import com.squareup.moshi.Json
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class UserRealm() : RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var avatarUrl: String = ""
    var createdAt: String = ""
    var updatedAt: String = ""

    constructor(user : UserRes) : this() {
       this.id = user.id
       this.firstName= user.firstName
       this.lastName= user.lastName
       this.email= user.email
       this.avatarUrl= user.avatarUrl
       this.createdAt= user.createdAt
       this.updatedAt= user. updatedAt
    }

}