package com.applications.whazzup.flowmortarapplication.data.storage.dto

import com.applications.whazzup.flowmortarapplication.data.storage.realm.UserRealm


class UserDto {
    var id: Int = -1
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var avatarUrl: String = ""

    constructor(user : UserRealm){
        this.id = user.id
        this.firstName= user.firstName
        this.lastName= user.lastName
        this.email= user.email
        this.avatarUrl= user.avatarUrl
    }
}