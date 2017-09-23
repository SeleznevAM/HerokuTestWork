package com.applications.whazzup.flowmortarapplication.data.network

import com.applications.whazzup.flowmortarapplication.data.network.req.UserReq
import com.applications.whazzup.flowmortarapplication.data.network.res.UserAvatarRes
import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import io.reactivex.Completable

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RestService {

    @GET("users.json")
    fun getUserFromNetwork() : Observable<Response<List<UserRes>>>

    @Multipart
    @POST
    fun uploadPhoto(@Url serverUrl : String, @Part file : MultipartBody.Part, @Header("Authorization") userToken : String) : Single<UserAvatarRes>

    @POST("users.json")
    fun createUser(@Body userReq : UserReq) : Single<UserRes>
    @PATCH("users/{id}.json")
    fun updateUser(@Path("id") id: Int, @Body userReq: UserReq) : Single<UserRes>

}