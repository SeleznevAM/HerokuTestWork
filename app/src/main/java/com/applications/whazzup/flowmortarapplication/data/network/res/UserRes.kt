package com.applications.whazzup.flowmortarapplication.data.network.res

import com.squareup.moshi.Json


class UserRes(@Json(name = "id") var id : Int,
              @Json(name = "first_name") var firstName : String,
              @Json(name = "last_name") var lastName : String,
              @Json(name = "email") var email : String,
              @Json(name = "avatar_url") var avatarUrl : String,
              @Json(name = "created_at") var createdAt : String,
              @Json(name = "updated_at") var updatedAt: String)