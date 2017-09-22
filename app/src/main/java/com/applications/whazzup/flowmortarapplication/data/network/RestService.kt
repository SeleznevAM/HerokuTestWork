package com.applications.whazzup.flowmortarapplication.data.network

import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface RestService {
    @GET("users.json")
    fun getUserFromNetwork() : Observable<Response<List<UserRes>>>

}