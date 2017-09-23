package com.applications.whazzup.flowmortarapplication.data.managers

import com.applications.whazzup.flowmortarapplication.App
import com.applications.whazzup.flowmortarapplication.data.network.RestService
import com.applications.whazzup.flowmortarapplication.data.network.req.UserReq
import com.applications.whazzup.flowmortarapplication.data.network.res.UserAvatarRes
import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import com.applications.whazzup.flowmortarapplication.di.components.DaggerDataManagerComponent
import com.applications.whazzup.flowmortarapplication.di.modules.NetworkModule
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject


class DataManager {

    @Inject
    lateinit var restService : RestService

     private val SERVER_URL = "http://207.154.248.163:5000/user/59450f6b4a96324fc6fae5b2/image/upload"
    private val USER_TOKEN = "7f45cf2d60cad5543ae2901208aa064bd1f9dd84"

    init {
        DaggerDataManagerComponent.builder().appComponent(App.appComponent).networkModule(NetworkModule()).build().inject(this)
    }

    fun getUserFromServerAndSaveInBD(): Observable<List<UserRes>?>? {
        return restService.getUserFromNetwork().flatMap { Observable.just(it.body()) }
    }

    fun uploadPhoto(body: MultipartBody.Part): Single<UserAvatarRes> {
        return restService.uploadPhoto(SERVER_URL, body, USER_TOKEN)
    }

    fun createUSer(userReq: UserReq): Single<UserRes> {
        return restService.createUser(userReq)
    }

    fun updateUser(id: Int, userReq: UserReq): Single<UserRes> {
        return restService.updateUser(id, userReq)
    }

}