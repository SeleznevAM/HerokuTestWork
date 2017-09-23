package com.applications.whazzup.flowmortarapplication.data.network.req

import com.squareup.moshi.Json

class UserReq(@Json(name = "first_name") var firstName : String,
              @Json(name = "last_name") var lastName : String,
              @Json(name = "email") var email : String,
              @Json(name = "avatar_url") var avatarUrl : String)